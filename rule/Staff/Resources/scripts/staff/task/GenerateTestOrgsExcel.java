package staff.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.server.Server;
import com.exponentus.util.ListUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;
import staff.dao.OrganizationDAO;
import staff.dao.OrganizationLabelDAO;
import staff.model.Organization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Command(name = "gen_test_orgs_xls")
public class GenerateTestOrgsExcel extends Do {
    private static String excelFile = EnvConst.RESOURCES_DIR + File.separator + "Fortune 500-US-List-Year-2010.xls";

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        Map<String, Organization> entities = new HashMap<String, Organization>();
        try {
            File xf = new File(excelFile);
            if (xf.exists()) {
                WorkbookSettings ws = new WorkbookSettings();
                //ws.setEncoding("Cp1252");
                Workbook workbook = null;
                try {
                    workbook = Workbook.getWorkbook(xf, ws);
                } catch (BiffException | IOException e) {
                    System.out.println(e);
                }
                OrgCategoryDAO ocDao = new OrgCategoryDAO(ses);
                OrganizationLabelDAO olDao = new OrganizationLabelDAO(ses);
                //List<OrganizationLabel> l = olDao.findAll().getApprovalResult();
                Sheet sheet = workbook.getSheet(0);
                int rCount = sheet.getRows();
                for (int i = 2; i < rCount; i++) {
                    String orgName = sheet.getCell(1, i).getContents();
                    if (!orgName.equals("") && !orgName.equals("''")) {
                        Organization entity = new Organization();
                        entity.setName(orgName);
                        Map<LanguageCode, String> localizedNames = new HashMap<>();
                        localizedNames.put(LanguageCode.RUS, orgName);
                        localizedNames.put(LanguageCode.KAZ, orgName);
                        localizedNames.put(LanguageCode.ENG, orgName);
                        entity.setLocName(localizedNames);
                        entity.setOrgCategory((OrgCategory) ListUtil.getRndListElement(ocDao.findAll().getResult()));
                        //List<OrganizationLabel> labels = new ArrayList<>();
                        //labels.add((OrganizationLabel) ListUtil.getRndListElement(l));
                        //entity.setLabels(labels);
                        entity.setTitle(orgName);
                        entities.put(orgName, entity);
                    }
                }
            } else {
                System.out.println("There is no \"" + excelFile + "\" file");
            }
        } catch (DAOException e) {
            Server.logger.exception(e);
        }

        try {
            OrganizationDAO oDao = new OrganizationDAO(ses);
            for (Entry<String, Organization> entry : entities.entrySet()) {
                save(oDao, entry.getValue(), entry.getKey());
            }
        } catch (

                DAOException e) {
            logger.exception(e);
        }

        logger.info("done...");

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void save(IDAO dao, IAppEntity entity, String extKey) {

        try {
            if (entity.getId() == null) {
                if (dao.add(entity) != null) {
                    logger.info(entity.getTitle() + " added");
                }
            } else {
                if (dao.update(entity) != null) {
                    logger.info(entity.getTitle() + " updated");
                }
            }
        } catch (DAOException e) {
            if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                logger.warning("a data is already exists (" + entity.getTitle() + "), record was skipped");
            } else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
                logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
            } else {
                logger.exception(e);
            }
        } catch (SecureException e) {
            logger.exception(e);
        }
    }

}
