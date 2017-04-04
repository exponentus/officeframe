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

import reference.dao.PositionDAO;
import reference.model.Position;

@Command(name = "fill_positions")
public class FillPositions extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Position> entities = new ArrayList<Position>();
		String[] data = { "CEO", "Manager", "Accounter", "Engineer", "Specialist", "Secretary", "Administrator",
				"Department manager", "Forwarder" };

		String[] dataRus = { "Директор", "Менеджер", "Бухгалтер", "Инженер", "Специалист", "Секретарь-референт",
				"Администратор", "Руководитель подразделения", "Экспедитор" };

		for (int i = 0; i < data.length; i++) {
			Position entity = new Position();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, data[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			PositionDAO dao = new PositionDAO(ses);
			for (Position entry : entities) {
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
