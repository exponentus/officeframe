package dataexport.services;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.appenv.AppEnv;
import com.exponentus.common.domain.IValidation;
import com.exponentus.common.other.IDataObtainer;
import com.exponentus.common.service.AbstractService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions.ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.log.Lg;
import com.exponentus.rest.exception.RestServiceException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scheduler.tasks.TempFileCleaner;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.util.ReflectionUtil;
import com.exponentus.util.TimeUtil;
import dataexport.dao.ReportProfileDAO;
import dataexport.model.ReportProfile;
import dataexport.model.constants.ExportFormatType;
import dataexport.model.constants.ReportQueryType;
import dataexport.other.RegistryDataObtainer;
import dataexport.ui.ActionFactory;
import dataexport.ui.ViewOptions;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import staff.dao.EmployeeDAO;
import staff.model.embedded.Observer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.*;

@Path("report-profiles")
@Produces(MediaType.APPLICATION_JSON)
public class ReportProfileService extends AbstractService<ReportProfile> {

    private static final String REPORT_NAME_KEYWORD = "report";
    private static final String JASPER_REPORT_TEMPLATE_EXTENSION = "jrxml";

    @GET
    public Response getViewPage() {
        try {
            _Session session = getSession();
            WebFormData params = getWebFormData();

            int pageSize = session.getPageSize();
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            ReportProfileDAO dao = new ReportProfileDAO(session);

            ViewPage vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            vp.setViewPageOptions(new ViewOptions().getReportProfileOptions());

            ActionFactory action = new ActionFactory();
            ActionBar actionBar = new ActionBar(session);
            actionBar.addAction(action.refreshVew);
            actionBar.addAction(action.addNew);
            actionBar.addAction(action.deleteDocument);

            Outcome outcome = new Outcome();
            outcome.setId("report-profiles");
            outcome.setTitle("report_profiles");
            outcome.setPayloadTitle("report_profiles");
            outcome.addPayload(actionBar);
            outcome.addPayload(vp);
            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") String id) {
        _Session session = getSession();
        try {
            ReportProfileDAO dao = new ReportProfileDAO(session);
            ReportProfile entity;

            boolean isNew = "new".equals(id);
            if (isNew) {
                entity = new ReportProfile();
                entity.setAuthor(session.getUser());
            } else {
                entity = dao.findById(id);
                if (entity == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            ActionFactory actionFactory = new ActionFactory();
            ActionBar actionBar = new ActionBar(session);
            actionBar.addAction(actionFactory.close);
            actionBar.addAction(actionFactory.saveAndClose);

            if (!isNew) {
                actionBar.addAction(actionFactory.toForm);
            }

            List<String> entityClassNames = new ArrayList<>();
            List<String> reportProfileClassNames = new ArrayList<>();
            for (AppEnv env : Environment.getApplications()) {
                String packageName = env.getPackageName();
                for (Class<IAppEntity<UUID>> appEntityClass : ReflectionUtil.getAllAppEntities(packageName)) {
                    entityClassNames.add(appEntityClass.getCanonicalName());
                }
                for (Class<IDataObtainer> reportProfileClass : ReflectionUtil.getAllReportImpls(packageName)) {
                    reportProfileClassNames.add(reportProfileClass.getCanonicalName());
                }
            }

            Outcome outcome = getOutcome(entity);
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.setPayloadTitle("report_profile");
            outcome.addPayload(actionBar);
            // include
            outcome.addPayload("exportFormatType", ExportFormatType.values());
            outcome.addPayload("reportQueryType", ReportQueryType.values());
            outcome.addPayload("entityClassNames", entityClassNames);
            outcome.addPayload("reportProfileClassNames", reportProfileClassNames);
            outcome.addPayload("languages", new LanguageDAO(session).findAllActivated());

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @Override
    public Response saveForm(ReportProfile dto) {
        try {
            ReportProfileDAO dao = new ReportProfileDAO(getSession());
            ReportProfile entity = fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
            dao.save(entity);

            Outcome outcome = getOutcome(entity);
            return Response.ok(outcome).build();
        } catch (DTOException e) {
            return responseValidationError(e);
        } catch (DAOException | SecureException e) {
            return responseException(e);
        }
    }

    @Override
    public ReportProfile fillFromDto(ReportProfile dto, IValidation<ReportProfile> validation, String formSesId) throws DTOException, DAOException {
        validation.check(dto);

        ReportProfile entity;

        if (dto.isNew()) {
            entity = new ReportProfile();
        } else {
            entity = dao.findById(dto.getId());
        }

        entity.setName(dto.getName());
        entity.setTitle(dto.getTitle());
        entity.setReportQueryType(dto.getReportQueryType());
        if (entity.getReportQueryType() == ReportQueryType.ENTITY_REQUEST || entity.getReportQueryType() == ReportQueryType.CUSTOM_CLASS) {
            entity.setClassName(dto.getClassName());
        } else {
            entity.setClassName("");
        }
        entity.setOutputFormat(dto.getOutputFormat());
        entity.setStartFrom(dto.getStartFrom());
        entity.setEndUntil(dto.getEndUntil());
        entity.setTags(dto.getTags());
        Map<LanguageCode, String> locNames = dto.getLocName();
        if (locNames.size() > 0) {
            entity.setLocName(dto.getLocName());
        } else {
            Map<LanguageCode, String> name = new HashMap<>();
            for (Language language : new LanguageDAO(getSession()).findAllActivated()) {
                name.put(language.getCode(), Environment.vocabulary.getWord(REPORT_NAME_KEYWORD, language.getCode()) + "-" + entity.getTitle());
            }
            entity.setLocName(name);
        }
        entity.setLocalizedDescr(dto.getLocalizedDescr());
        entity.setObservers(dto.getObservers());

        EmployeeDAO eDao = new EmployeeDAO(getSession());

        List<staff.model.embedded.Observer> observers = new ArrayList<staff.model.embedded.Observer>();
        for (staff.model.embedded.Observer o : dto.getObservers()) {
            staff.model.embedded.Observer observer = new Observer();
            observer.setEmployee(eDao.findById(o.getEmployee().getId()));
            observers.add(observer);
        }
        entity.setObservers(observers);

        if (entity.isNew()) {
            entity.setAuthor(getSession().getUser());
        }
        entity.setAttachments(getActualAttachments(entity.getAttachments(), dto.getAttachments()));
        // calculateReadersEditors(entity);
        return entity;
    }

    @POST
    @Path("action/toForm")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response toForm(ReportProfile dto) {
        long start_time = System.currentTimeMillis();
        String reportTemplateName = null;
        String type = dto.getOutputFormat().name();
        String reportFileName = null, appCode = null;

        try {
            _Session session = getSession();
            ReportProfileDAO reportProfileDAO = new ReportProfileDAO(session);
            ReportProfile profile = reportProfileDAO.findById(dto.getId());
            if (profile != null) {
                if (!"xlsx".equalsIgnoreCase(type) && !"pdf".equalsIgnoreCase(type)
                        && !"xml".equalsIgnoreCase(type) && !"csv".equalsIgnoreCase(type)) {
                    throw new IllegalArgumentException("Unsupported format: " + type + "; Supported format: pdf, xlsx");
                }
                String repPath = Environment.getOfficeFrameDir() + File.separator + "modules" + File.separator
                        + getAppEnv().appName + File.separator + "Resources" + File.separator + "report";

                HashMap<String, Object> parameters = new HashMap<>();
                JRFileVirtualizer virtualizer = new JRFileVirtualizer(10, Environment.trash);
                parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

                LanguageCode lang = session.getLang();
                IDataObtainer obtainer = null;
                List result = new ArrayList<>();

                switch (profile.getReportQueryType()) {
                    case ENTITY_REQUEST:
                        obtainer = new RegistryDataObtainer();
                        obtainer.setSession(session);
                        reportTemplateName = obtainer.getTemplateName();
                        appCode = obtainer.getAppCode();
                        result = obtainer.getReportData(dto.getStartFrom(), dto.getEndUntil(), profile.getClassName());
                        reportFileName = profile.getClassName() + "_" + obtainer.getReportFileName();
                        parameters.put("entity_name", dto.getClassName());
                        break;
                    case CUSTOM_CLASS:
                        try {
                            Class clazz = Class.forName(profile.getClassName());
                            if (IDataObtainer.class.isAssignableFrom(clazz)) {
                                obtainer = (IDataObtainer) clazz.newInstance();
                                obtainer.setTitle(profile.getLocName(session.getLang()));
                                obtainer.setSession(session);
                                obtainer.setDetails(TimeUtil.dateTimeToStringSilently(new Date()) + ", " +
                                        Environment.vocabulary.getWord("start_from", lang).toLowerCase() + ": " + TimeUtil.dateToStringSilently(dto.getStartFrom()) + " " +
                                        Environment.vocabulary.getWord("end_until", lang).toLowerCase() + ": " + TimeUtil.dateToStringSilently(dto.getEndUntil()));
                                result = obtainer.getReportData(dto.getStartFrom(), dto.getEndUntil(), "");
                                reportTemplateName = obtainer.getTemplateName();
                                appCode = obtainer.getAppCode();
                                reportFileName = obtainer.getReportFileName();
                            } else {
                                throw new RestServiceException("Improper implementation of \"" + IDataObtainer.class.getName() + "\"");
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RestServiceException("Class \"" + profile.getClassName() + "\", has not been found");
                        } catch (IllegalAccessException | InstantiationException e) {
                            return responseException(e);
                        }
                }

                parameters.put("title", obtainer.getTitle());
                parameters.put("details", obtainer.getDetails());

                JRBeanCollectionDataSource dSource = new JRBeanCollectionDataSource(result);
                JasperPrint print = JasperFillManager
                        .fillReport(
                                JasperCompileManager.compileReportToFile(repPath + File.separator + "templates"
                                        + File.separator + appCode + File.separator + reportTemplateName + "." + JASPER_REPORT_TEMPLATE_EXTENSION),
                                parameters, dSource);

                boolean needToWriteStream = false;
                String filePath = Environment.tmpDir + File.separator + reportFileName + "." + type;
                File reportFile = new File(filePath);

                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    Exporter exporter = null;
                    if (type.equalsIgnoreCase("pdf")) {
                        JRStyle style = new JRDesignStyle();
                        String pathToFont = repPath + File.separator + "templates" + File.separator + "fonts" + File.separator
                                + "tahoma.ttf";
                        style.setPdfFontName(pathToFont);
                        style.setPdfEncoding("Cp1251");
                        style.setPdfEmbedded(true);
                        print.setDefaultStyle(style);
                        exporter = new JRPdfExporter();
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                        needToWriteStream = true;
                    } else if (type.equalsIgnoreCase("xlsx")) {
                        exporter = new JRXlsxExporter();
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                        needToWriteStream = true;
                    } else if (type.equalsIgnoreCase("csv")) {
                        exporter = new JRCsvExporter();
                        exporter.setExporterOutput(new SimpleWriterExporterOutput(reportFile));
                    } else if (type.equalsIgnoreCase("xml")) {
                        exporter = new JRXmlExporter();
                        exporter.setExporterOutput(new SimpleWriterExporterOutput(reportFile));
                    } else {
                        throw new RestServiceException(type + " is unknown export format");
                    }

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.exportReport();

                    File someFile = new File(filePath);
                    if (needToWriteStream) {
                        FileOutputStream fos = null;
                        byte[] bytes = outputStream.toByteArray();
                        if (bytes.length > 1) {
                            fos = new FileOutputStream(someFile);
                            fos.write(bytes);
                            fos.flush();
                            fos.close();
                        }
                    }
                    String codedFileName = URLEncoder.encode(reportFileName + "." + type, "UTF8");
                    String val = someFile.getAbsolutePath();
                    Server.logger.info(
                            "Report \"" + reportTemplateName + "\" is ready, estimated time is " + TimeUtil.getTimeDiffInMilSec(start_time));

                    System.out.println(val);
                    return Response.ok(someFile, MediaType.APPLICATION_OCTET_STREAM).
                            header("Content-Disposition", "attachment; filename*=\"utf-8'" + codedFileName + "\"").build();
                } catch (Exception e) {
                    return responseException(e);
                } finally {
                    TempFileCleaner.addFileToDelete(filePath);
                }
            } else {
                return responseValidationError("ReportProfile entity has not been found id=" + dto.getId());
            }
        } catch (JRException e) {
            Lg.exception(e);
            if (e.getCause() instanceof FileNotFoundException) {
                Lg.exception(e);
                return responseException(
                        new FileNotFoundException(JASPER_REPORT_TEMPLATE_EXTENSION + " file has not been found"));
            }
            return responseException(e);
        } catch (IllegalArgumentException e) {
            return responseException(e);
        } catch (Exception e) {
            return responseException(e);
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete() {
        try {
            ReportProfileDAO dao = new ReportProfileDAO(getSession());
            String[] ids = getWebFormData().getListOfValuesSilently("id");
            for (String id : ids) {
                ReportProfile entity = dao.findById(id);
                if (entity != null) {
                    dao.delete(entity);
                }
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private class Validation implements IValidation<ReportProfile> {

        @Override
        public void check(ReportProfile dto) throws DTOException {
            DTOException ve = new DTOException();

            if (dto.getName() == null || dto.getName().isEmpty()) {
                ve.addError("name", "required", "field_is_empty");
            }

            if (ve.hasError()) {
                throw ve;
            }
        }
    }
}
