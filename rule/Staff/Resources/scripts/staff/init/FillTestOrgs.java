package staff.init;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.env.EnvConst;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;
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

/**
 *
 *
 * @author Kayra created 24-01-2016
 */

public class FillTestOrgs extends InitialDataAdapter<Organization, OrganizationDAO> {
	private static String excelFile = EnvConst.RESOURCES_DIR + File.separator + "orgs.xls";

	@Override
	public List<Organization> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<Organization> entities = new ArrayList<>();
		try {
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
				OrgCategoryDAO ocDao = new OrgCategoryDAO(ses);
				OrganizationLabelDAO olDao = new OrganizationLabelDAO(ses);
				List<OrganizationLabel> l = olDao.findAll().getResult();
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
						entity.setLocalizedName(localizedNames);
						entity.setOrgCategory((OrgCategory) ListUtil.getRndListElement(ocDao.findAll().getResult()));
						List<OrganizationLabel> labels = new ArrayList<>();
						labels.add((OrganizationLabel) ListUtil.getRndListElement(l));
						entity.setLabels(labels);
						entities.add(entity);
					}
				}
			} else {
				System.out.println("There is no \"" + excelFile + "\" file");
			}
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}
		return entities;
	}

}
