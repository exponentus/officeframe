package staff.init;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.env.EnvConst;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;
import com.exponentus.util.Util;

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
		List<Organization> entities = new ArrayList<Organization>();

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
			List<OrganizationLabel> l = olDao.findAll();
			Sheet sheet = workbook.getSheet(0);
			int rCount = sheet.getRows();
			for (int i = 2; i < rCount; i++) {
				String orgName = sheet.getCell(1, i).getContents();
				if (!orgName.equals("") && !orgName.equals("''")) {
					Organization entity = new Organization();
					entity.setName(orgName);
					entity.setOrgCategory((OrgCategory) Util.getRndListElement(ocDao.findAll()));
					List<OrganizationLabel> labels = new ArrayList<OrganizationLabel>();
					labels.add((OrganizationLabel) Util.getRndListElement(l));
					entity.setLabels(labels);
					entities.add(entity);
				}
			}
		} else {
			System.out.println("There is no \"" + excelFile + "\" file");
		}
		return entities;
	}

}
