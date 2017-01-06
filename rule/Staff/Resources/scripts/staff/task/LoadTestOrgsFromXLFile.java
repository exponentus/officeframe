package staff.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
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
import staff.model.OrganizationLabel;

@Command(name = "load_orgs_xls")
public class LoadTestOrgsFromXLFile extends _Do {
	private static String excelFile = EnvConst.RESOURCES_DIR + File.separator + "orgs.xls";

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Organization> entities = new ArrayList<>();
		try {
			OrgCategoryDAO ocDao = new OrgCategoryDAO(ses);
			OrganizationLabelDAO olDao = new OrganizationLabelDAO(ses);

			File xf = new File(excelFile);
			if (xf.exists()) {
				WorkbookSettings ws = new WorkbookSettings();
				ws.setEncoding("Cp1252");
				Workbook workbook = null;
				try {
					workbook = Workbook.getWorkbook(xf, ws);
				} catch (BiffException | IOException e) {
					System.out.println(e);
				}

				List<OrganizationLabel> l = olDao.findAll().getResult();
				Sheet sheet = workbook.getSheet(0);
				int rCount = sheet.getRows();
				for (int i = 2; i < rCount; i++) {
					String orgName = sheet.getCell(1, i).getContents();
					if (!orgName.equals("") && !orgName.equals("''")) {
						Organization entity = new Organization();
						entity.setName(orgName);
						entity.setOrgCategory((OrgCategory) ListUtil.getRndListElement(ocDao.findAll().getResult()));
						List<OrganizationLabel> labels = new ArrayList<>();
						labels.add((OrganizationLabel) ListUtil.getRndListElement(l));
						entity.setLabels(labels);
						entities.add(entity);
					}
				}
			} else {
				System.out.println("there is no \"" + excelFile + "\" file");
			}

			OrganizationDAO oDao = new OrganizationDAO(ses);
			for (Organization org : entities) {
				try {
					oDao.add(org);
					System.out.println(org.getName() + " added");
				} catch (SecureException | DAOException e) {
					logger.errorLogEntry(e);
				}
			}
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}
		System.out.println("done...");
	}

}
