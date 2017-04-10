package reference.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;

@Command(name = "fill_org_categories")
public class FillOrgCategories extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {

		List<OrgCategory> entities = new ArrayList<>();
		String[] data = { "LTD", "Self_employed", "JSC", "State_office", "State_enterprise", "International_company",
				"Public_association", "City_Hall", "Embassy", "Educational_institution" };
		String[] dataRus = { "ТОО", "Частный предприниматель", "АО", "Государственное ведомство", "РГП",
				"Зарубежная компания", "Общественное объединение", "Мэрия", "Посольство",
				"Образовательное учреждение" };

		for (int i = 0; i < data.length; i++) {
			OrgCategory entity = new OrgCategory();
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.ENG, data[i]);
			name.put(LanguageCode.KAZ, dataRus[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entity.setName(data[i]);
			entities.add(entity);
		}

		try {
			OrgCategoryDAO dao = new OrgCategoryDAO(ses);
			for (OrgCategory entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.infoLogEntry(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				} catch (SecureException e) {
					logger.errorLogEntry(e);
				}
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
		}
		logger.infoLogEntry("done...");
	}

}