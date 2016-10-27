package staff.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.exception.SecureException;
import com.exponentus.legacy.domino.DominoEnvConst;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPatch;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;
import reference.tasks.InsertUndefinedGag;
import staff.dao.OrganizationDAO;
import staff.dao.OrganizationLabelDAO;
import staff.model.Organization;
import staff.model.OrganizationLabel;

@Command(name = "import_orgs_nsf")
public class ImportOrgsFromNSF extends _DoPatch {
	private static final String PRIMARY_ORGANIZATION = "Банк развития Казахстана";
	private static final String PRIMARY_ORGANIZATION_LABEL = "primary";

	@Override
	public void doTask(_Session ses) {
		Map<String, Organization> entities = new HashMap<>();
		OrgCategoryDAO ocDao = new OrgCategoryDAO(ses);
		CollationDAO cDao = new CollationDAO(ses);
		Map<String, String> typeCorrCollation = typeCorrCollationMapInit();

		try {
			Session dominoSession = NotesFactory.createSession(DominoEnvConst.DOMINO_HOST, DominoEnvConst.DOMINO_USER,
			        DominoEnvConst.DOMINO_USER_PWD);
			Database inDb = dominoSession.getDatabase(dominoSession.getServerName(), DominoEnvConst.APPLICATION_DIRECTORY + "struct.nsf");
			View view = inDb.getView("(AllUNID)");
			ViewEntryCollection vec = view.getAllEntries();
			ViewEntry entry = vec.getFirstEntry();
			ViewEntry tmpEntry = null;
			while (entry != null) {
				Document doc = entry.getDocument();
				String form = doc.getItemValueString("Form");
				if (form.equals("Corr")) {
					Organization entity = new Organization();
					entity.setAuthor(new SuperUser());
					entity.setName(doc.getItemValueString("FullName"));
					Map<LanguageCode, String> localizedNames = new HashMap<>();
					localizedNames.put(LanguageCode.RUS, doc.getItemValueString("FullName"));
					entity.setLocalizedName(localizedNames);
					String typeCorr = doc.getItemValueString("TypeCorr");
					String intRefKey = typeCorrCollation.get(typeCorr);
					if (intRefKey == null) {
						logger.errorLogEntry("wrong reference ext value \"" + typeCorr + "\"");
						intRefKey = InsertUndefinedGag.gagKey;
					}
					OrgCategory oCat = ocDao.findByName(intRefKey);
					entity.setOrgCategory(oCat);

					entities.put(doc.getUniversalID(), entity);
				}
				tmpEntry = vec.getNextEntry();
				entry.recycle();
				entry = tmpEntry;
			}
		} catch (NotesException e) {
			logger.errorLogEntry(e);
		}

		logger.infoLogEntry("has been found " + entities.size() + " records");
		OrganizationDAO oDao = new OrganizationDAO(ses);
		for (Entry<String, Organization> entry : entities.entrySet()) {
			Organization org = entry.getValue();
			try {
				try {
					if (oDao.add(org) != null) {
						Collation collation = new Collation();
						collation.setExtKey(entry.getKey());
						collation.setIntKey(org.getId());
						collation.setEntityType(org.getClass().getName());
						cDao.add(collation);
					}
					System.out.println(org.getName() + " added");
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				}

			} catch (SecureException e) {
				logger.errorLogEntry(e);
			}
		}
		setPrimaryOrg(oDao);

		logger.infoLogEntry("done...");
	}

	private void setPrimaryOrg(OrganizationDAO oDao) {
		logger.infoLogEntry("setup primary organization...");
		ViewPage<Organization> orgs = oDao.findAllByKeyword(PRIMARY_ORGANIZATION, 1, 1);
		Organization org = orgs.getResult().get(0);
		if (org != null) {
			OrganizationLabelDAO olDao = new OrganizationLabelDAO(ses);
			List<OrganizationLabel> labels = new ArrayList<>();
			OrganizationLabel l = olDao.findByName(PRIMARY_ORGANIZATION_LABEL);
			if (l != null) {
				labels.add(l);
				org.setLabels(labels);
				try {
					oDao.update(org);
				} catch (SecureException e) {
					logger.errorLogEntry(e);
				} catch (DAOException e) {
					logger.errorLogEntry(e);
				}
			} else {
				logger.errorLogEntry("organization label has not been found (" + PRIMARY_ORGANIZATION_LABEL + ")");
			}
		} else {
			logger.errorLogEntry("primary organization has not been found (" + PRIMARY_ORGANIZATION + ")");
		}
	}

	private Map<String, String> typeCorrCollationMapInit() {
		Map<String, String> typeCorrCollation = new HashMap<>();
		typeCorrCollation.put("ТОО", "LTD");
		typeCorrCollation.put("Банки", "Bank");
		typeCorrCollation.put("Компании", "LTD");
		typeCorrCollation.put("Компания", "LTD");
		typeCorrCollation.put("компания", "LTD");
		typeCorrCollation.put("ООО", "LTD");
		typeCorrCollation.put("Фирма", "LTD");
		typeCorrCollation.put("Фирмы", "LTD");
		typeCorrCollation.put("GmbH", "LTD");
		typeCorrCollation.put("Кооператив", "LTD");
		typeCorrCollation.put("Кооператив", "LTD");
		typeCorrCollation.put("АО", "JSC");
		typeCorrCollation.put("ГУ", "State_office");
		typeCorrCollation.put("ИНТЕГРАЦИЯ", "Integration");
		typeCorrCollation.put("ОАО", "JSC");
		typeCorrCollation.put("Министерства РК", "Ministry");
		typeCorrCollation.put("Премьер-Министр РК", "Ministry");
		typeCorrCollation.put("Суд", "Court");
		typeCorrCollation.put("Фонды", "Court");
		typeCorrCollation.put("РГП", "State_enterprise");
		typeCorrCollation.put("ГКП", "State_enterprise");
		typeCorrCollation.put("РГКП", "State_enterprise");
		typeCorrCollation.put("Комитеты", "Committee");
		typeCorrCollation.put("Зарубежная компания", "International_company");
		typeCorrCollation.put("Президент РК", "State_enterprise");
		typeCorrCollation.put("финансовая полиция", "State_enterprise");
		typeCorrCollation.put("Прокуратура", "State_enterprise");
		typeCorrCollation.put("Агентства РК", "State_enterprise");
		typeCorrCollation.put("Агентства", "State_enterprise");
		typeCorrCollation.put("Агентство", "State_enterprise");
		typeCorrCollation.put("Управление", "State_enterprise");
		typeCorrCollation.put("Управления", "State_enterprise");
		typeCorrCollation.put("Министрества РК", "State_enterprise");
		typeCorrCollation.put("Правительство", "State_enterprise");
		typeCorrCollation.put("ЦОН", "State_enterprise");
		typeCorrCollation.put("Акиматы", "City_Hall");
		typeCorrCollation.put("ЗАО", "JSC");
		typeCorrCollation.put("ИП", "Self_employed");
		typeCorrCollation.put("Предприниматель", "Self_employed");
		typeCorrCollation.put("Физические лица", "Self_employed");
		typeCorrCollation.put("Союзы", "Public_association");
		typeCorrCollation.put("Филиал", "Branch");
		typeCorrCollation.put("Посольство", "Embassy");
		typeCorrCollation.put("Посольство в РК", "Embassy");
		typeCorrCollation.put("Посольства РК за рубежом", "Embassy");
		typeCorrCollation.put("Университет", "Educational_institution");

		typeCorrCollation.put("null", InsertUndefinedGag.gagKey);
		typeCorrCollation.put("", InsertUndefinedGag.gagKey);
		return typeCorrCollation;

	}

}
