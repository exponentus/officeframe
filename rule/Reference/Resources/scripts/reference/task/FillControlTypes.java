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

import reference.dao.ControlTypeDAO;
import reference.model.ControlType;
import reference.model.constants.ControlSchemaType;

@Command(name = "fill_control_types")
public class FillControlTypes extends Do {
	private static final int HOURS_TO_DO = 24 * 30;

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<ControlType> entities = new ArrayList<>();

		String codes[] = { "HU", "FI", "C" };
		String names[] = { "very_urgent", "for_information", "in_control" };
		String namesEng[] = { "Highly urgent", "For information", "In control" };
		String namesRus[] = { "Весьма срочно", "Для сведения", "На контроле" };
		String namesKaz[] = { "Жедел бақылауда", "Для сведения", "На контроле" };
		for (int i = 0; i < codes.length; i++) {
			ControlType cType = new ControlType();
			cType.setCode(codes[i]);
			cType.setName(names[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.KAZ, namesKaz[i]);
			name.put(LanguageCode.RUS, namesRus[i]);
			name.put(LanguageCode.ENG, namesEng[i]);
			cType.setLocName(name);
			cType.setDefaultHours(HOURS_TO_DO);
			if (codes[i].equals("FI")) {
				cType.setSchema(ControlSchemaType.ALLOW_RESET_ON_BASIS_REPORT);
			} else {
				cType.setSchema(ControlSchemaType.RESET_ALL_MANULALLY);
			}
			entities.add(cType);
		}

		try {
			ControlTypeDAO dao = new ControlTypeDAO(ses);
			for (ControlType entry : entities) {
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
