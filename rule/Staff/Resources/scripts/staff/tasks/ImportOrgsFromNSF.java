package staff.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.legacy.ConvertorEnvConst;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.server.Server;
import com.exponentus.user.SuperUser;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;
import staff.dao.OrganizationDAO;
import staff.dao.OrganizationLabelDAO;
import staff.model.Organization;
import staff.model.OrganizationLabel;

@Command(name = "import_orgs_nsf")
public class ImportOrgsFromNSF extends ImportNSF {
	private static final String PRIMARY_ORGANIZATION = "Банк развития Казахстана".toLowerCase();
	private static final String PRIMARY_ORGANIZATION_LABEL = "primary";
	
	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Map<String, Organization> entities = new HashMap<>();
		try {
			OrgCategoryDAO ocDao = new OrgCategoryDAO(ses);
			OrganizationDAO oDao = new OrganizationDAO(ses);
			Map<String, String> typeCorrCollation = typeCorrCollationMapInit();
			
			try {
				ViewEntryCollection vec = getAllEntries("struct.nsf");
				ViewEntry entry = vec.getFirstEntry();
				ViewEntry tmpEntry = null;
				while (entry != null) {
					Document doc = entry.getDocument();
					String form = doc.getItemValueString("Form");
					if (form.equals("Corr")) {
						try {
							String unId = doc.getUniversalID();
							Organization entity = oDao.findByExtKey(unId);
							if (entity == null) {
								entity = new Organization();
								entity.setAuthor(new SuperUser());
							}
							String orgName = doc.getItemValueString("FullName");
							entity.setName(orgName);
							Map<LanguageCode, String> localizedNames = new HashMap<>();
							localizedNames.put(LanguageCode.RUS, orgName);
							localizedNames.put(LanguageCode.KAZ, doc.getItemValueString("FullNameKZ"));
							localizedNames.put(LanguageCode.ENG, orgName);
							entity.setLocalizedName(localizedNames);
							String typeCorr = doc.getItemValueString("TypeCorr");
							String intRefKey = typeCorrCollation.get(typeCorr);
							if (intRefKey == null) {
								logger.errorLogEntry("wrong reference ext value \"" + typeCorr + "\"");
								intRefKey = ConvertorEnvConst.GAG_KEY;
							}
							OrgCategory oCat = ocDao.findByName(intRefKey);
							entity.setOrgCategory(oCat);
							if (entity.getName().toLowerCase().contains(PRIMARY_ORGANIZATION)) {
								List<OrganizationLabel> labels = new ArrayList<>();
								OrganizationLabelDAO olDao = new OrganizationLabelDAO(ses);
								OrganizationLabel l = olDao.findByName(PRIMARY_ORGANIZATION_LABEL);
								if (l != null) {
									labels.add(l);
									entity.setLabels(labels);
								}
							}
							entities.put(doc.getUniversalID(), entity);
						} catch (DAOException e) {
							logger.errorLogEntry(e);
						}
					}
					tmpEntry = vec.getNextEntry();
					entry.recycle();
					entry = tmpEntry;
				}
			} catch (NotesException e) {
				logger.errorLogEntry(e);
			}
			
			logger.infoLogEntry("has been found " + entities.size() + " records");
			for (Entry<String, Organization> entry : entities.entrySet()) {
				save(oDao, entry.getValue(), entry.getKey());
			}
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}
		logger.infoLogEntry("done...");
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
		
		typeCorrCollation.put("null", ConvertorEnvConst.GAG_KEY);
		typeCorrCollation.put("", ConvertorEnvConst.GAG_KEY);
		return typeCorrCollation;
		
	}
	
}
