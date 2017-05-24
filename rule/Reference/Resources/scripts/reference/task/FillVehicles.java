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
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.VehicleDAO;
import reference.model.Vehicle;

@Command(name = "fill_vehicles")
public class FillVehicles extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Vehicle> entities = new ArrayList<>();

		String names[] = { "trabant", "scania", "gaz", "niva" };
		String namesEng[] = { "Trabant", "Scania", "Gaz", "Niva" };
		String namesRus[] = { "Трабант", "Скания", "Газ-24", "Нива Ваз-2121" };
		for (int i = 0; i < names.length; i++) {
			Vehicle entity = new Vehicle();
			entity.setName(names[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.RUS, namesRus[i]);
			name.put(LanguageCode.ENG, namesEng[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			VehicleDAO dao = new VehicleDAO(ses);
			for (Vehicle entry : entities) {
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
