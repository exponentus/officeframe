package dataexport.services;

import com.exponentus.appenv.AppEnv;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.domain.IValidation;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.scheduler.tasks.TempFileCleaner;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.server.Server;
import com.exponentus.util.ReflectionUtil;
import com.exponentus.util.StringUtil;
import com.exponentus.util.TimeUtil;
import dataexport.dao.ReportProfileDAO;
import dataexport.domain.ReportProfileDomain;
import dataexport.init.AppConst;
import dataexport.model.ReportProfile;
import dataexport.model.constants.ExportFormatType;
import dataexport.model.constants.ReportQueryType;
import dataexport.ui.ActionFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import reference.model.Position;
import staff.model.Employee;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Path("report-profiles")
@Produces(MediaType.APPLICATION_JSON)
public class ReportProfileService extends EntityService<ReportProfile, ReportProfileDomain> {
    private static final String JASPER_REPORT_TEMPLATE_EXTENSION = "jrxml";

    @GET
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        int pageSize = session.getPageSize();
        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));

        ViewPage vp = getDomain().getViewPage(sortParams, params.getPage(), pageSize);

        _ActionBar actionBar = new _ActionBar(session);
        actionBar.addAction(new ConventionalActionFactory().refreshVew);

        Outcome outcome = new Outcome();
        outcome.setId("report-profiles");
        outcome.setTitle("report_profiles");
        outcome.addPayload("contentTitle", "report_profiles");
        outcome.addPayload(actionBar);
        outcome.addPayload(vp);
        return Response.ok(outcome).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") String id) {
        _Session session = getSession();
        try {
            ReportProfileDAO dao = new ReportProfileDAO(session);
            ReportProfileDomain domain = new ReportProfileDomain(session);
            ReportProfile entity;

            boolean isNew = "new".equals(id);
            if (isNew) {
                entity = domain.composeNew(session.getUser());
            } else {
                entity = dao.findByIdentefier(id);
                if (entity == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            ActionFactory actionFactory = new ActionFactory();
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(actionFactory.close);
            actionBar.addAction(actionFactory.saveAndClose);
            actionBar.addAction(actionFactory.toForm);

            List<String> entityClassNames = new ArrayList<>();
            for (AppEnv env : Environment.getApplications()) {
                for (Class<IAppEntity<UUID>> _entity : ReflectionUtil.getAllAppEntities(env.getPackageName())) {
                    String entityClassName = _entity.getCanonicalName();
                    entityClassNames.add(entityClassName);
                }
            }

            Outcome outcome = domain.getOutcome(entity);
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload("contentTitle", "report_profile");
            outcome.addPayload("exportFormatType", ExportFormatType.values());
            outcome.addPayload("reportQueryType", ReportQueryType.values());
            outcome.addPayload("entityClassNames", entityClassNames);
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @Override
    public Response saveForm(ReportProfile dto) {
        try {
            ReportProfileDomain domain = new ReportProfileDomain(getSession());
            ReportProfile entity = domain.fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
            domain.save(entity);

            Outcome outcome = domain.getOutcome(entity);

            return Response.ok(outcome).build();
        } catch (DTOException e) {
            return responseValidationError(e);
        } catch (DAOException | SecureException e) {
            return responseException(e);
        }
    }

    @POST
    @Path("action/toForm")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response toForm(ReportProfile dto) {
        long start_time = System.currentTimeMillis();
        dto.setEntityName(Employee.class.getCanonicalName());
        String reportId = dto.getId().toString();
        String name = "entity_registry";
        String type = dto.getOutputFormat().name();
        type = "pdf";

        try {
            if (!"xlsx".equals(type) && !"pdf".equals(type)) {
                throw new IllegalArgumentException("Unsupported format: " + type + "; Supported format: pdf, xlsx");
            }

            HashMap<String, Object> parameters = new HashMap<>();

            String repPath = Environment.getOfficeFrameDir() + File.separator + "rule" + File.separator
                    + getAppEnv().appName + File.separator + "Resources" + File.separator + "report";

            JRFileVirtualizer virtualizer = new JRFileVirtualizer(10, Environment.trash);
            parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
            _Session session = getSession();
            IDAO dao = DAOFactory.get(session, dto.getEntityName());
            List<Position> result = dao.findAll().getResult();

            JRBeanCollectionDataSource dSource = new JRBeanCollectionDataSource(result);
            JasperPrint print = JasperFillManager
                    .fillReport(
                            JasperCompileManager.compileReportToFile(repPath + File.separator + "templates"
                                    + File.separator + AppConst.CODE + File.separator + name + "." + JASPER_REPORT_TEMPLATE_EXTENSION),
                            parameters, dSource);

            String filePath = Environment.tmpDir + File.separator
                    + StringUtil.generateRndAsText("qwertyuiopasdfghjklzxcvbnm", 10) + "." + type;
            if (type.equals("pdf")) {
                JRStyle style = new JRDesignStyle();
                style.setPdfFontName(repPath + File.separator + "templates" + File.separator + "fonts" + File.separator
                        + "tahoma.ttf");
                style.setPdfEncoding("Cp1251");
                style.setPdfEmbedded(true);
                print.setDefaultStyle(style);
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
                exporter.exportReport();
            } else if (type.equals("xlsx")) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
                exporter.exportReport();
            }
            Server.logger.info(
                    "Report \"" + name + "\" is ready, estimated time is " + TimeUtil.getTimeDiffInMilSec(start_time));


            try {
                File file = new File(filePath);
                String codedFileName = URLEncoder.encode(file.getName(), "UTF8");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachment; filename*=\"utf-8'" + codedFileName + "\"").build();

            } catch (Exception e) {
                return responseException(e);
            } finally {
                TempFileCleaner.addFileToDelete(filePath);
            }
        } catch (JRException e) {
            logError(e);
            if (e.getCause() instanceof FileNotFoundException) {
                logError(e);
                return responseException(
                        new FileNotFoundException(JASPER_REPORT_TEMPLATE_EXTENSION + " file has not been found"));
            }
            return responseException(e);
        } catch (IllegalArgumentException e) {
            return responseException(e);
        }
    }

    private class Validation implements IValidation<ReportProfile> {

        @Override
        public void check(ReportProfile dto) throws DTOException {
            DTOException ve = new DTOException();

            if (ve.hasError()) {
                throw ve;
            }
        }
    }
}
