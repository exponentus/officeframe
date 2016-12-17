package reference.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.DemandTypeDAO;
import reference.model.DemandType;

@Command(name = "fill_demand_types")
public class FillDemandTypes extends _Do {
	
	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<DemandType> entities = new ArrayList<>();

		String names[] = { "bug", "coding", "recommendation", "wish", "clarify" };
		String namesEng[] = { "Bug report", "Demand to coding", "Recommendation", "Wish", "Demand to clarify" };
		String namesRus[] = { "Сообщение об ошибке", "Запрос на доработку", "Рекомендация", "Пожелание",
				"Запрос на разъяснение" };
		String namesKaz[] = { "Сообщение об ошибке", "Запрос на доработку", "Рекомендация", "Пожелание",
				"Запрос на разъяснение" };
		for (int i = 0; i < names.length; i++) {
			DemandType entity = new DemandType();
			entity.setName(names[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.ENG, namesEng[i]);
			name.put(LanguageCode.KAZ, namesKaz[i]);
			name.put(LanguageCode.RUS, namesRus[i]);
			entity.setLocalizedName(name);
			entities.add(entity);
		}

		try {
			DemandTypeDAO dao = new DemandTypeDAO(ses);
			for (DemandType entry : entities) {
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
