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

import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;

@Command(name = "fill_department_types")
public class FillDepartmentTypes extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {

		List<DepartmentType> entities = new ArrayList<>();
		String[] data = { "Department", "Sector", "Group", "Management" };
		String[] dataRus = { "Департамент", "Сектор", "Группа", "Управление" };
		String[] dataKaz = { "Департамент", "Сектор", "Группа", "Управление" };

		for (int i = 0; i < data.length; i++) {
			DepartmentType entity = new DepartmentType();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.ENG, data[i]);
			name.put(LanguageCode.KAZ, dataKaz[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			DepartmentTypeDAO dao = new DepartmentTypeDAO(ses);
			for (DepartmentType entry : entities) {
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
