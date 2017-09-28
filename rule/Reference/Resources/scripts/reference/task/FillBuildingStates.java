package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.BuildingStateDAO;
import reference.dao.RealEstateObjStatusDAO;
import reference.init.AppConst;
import reference.model.BuildingState;
import reference.model.RealEstateObjStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_building_states")
public class FillBuildingStates extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<BuildingState> entities = new ArrayList<>();

		String names[] = { "in_operation", "commissioned", "under_construction" };
		String namesEng[] = { "In operation", "Commissioned", "Under construction" };
		String namesRus[] = { "В эксплуатации", "Вводимый в эксплуатацию","Строящиеся"};
		String namesKaz[] = { "Қолданыста", "Тапсырылжатыр", "Кұрылыс барысында"};

		for (int i = 0; i < names.length; i++) {
			BuildingState status = new BuildingState();
			status.setName(names[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.RUS, namesRus[i]);
			name.put(LanguageCode.ENG, namesEng[i]);
			name.put(LanguageCode.KAZ, namesKaz[i]);
			status.setLocName(name);
			entities.add(status);
		}

		try {
			BuildingStateDAO dao = new BuildingStateDAO(ses);
			for (BuildingState entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.info(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				} catch (SecureException e) {
					logger.exception(e);
				}
			}
		} catch (DAOException e) {
			logger.exception(e);
		}
		logger.info("done...");
	}

}
