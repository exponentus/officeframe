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

import reference.dao.ControlTypeDAO;
import reference.model.ControlType;

@Command(name = "fill_control_types")
public class FillControlTypes extends _Do {
	private static final int HOURS_TO_DO = 24 * 30;

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<ControlType> entities = new ArrayList<>();

		String codes[] = { "HU", "FM", "FI", "P", "C", "NC", "UC" };
		String names[] = { "very_urgent", "for_meeting", "for_information", "to_participate", "in_control",
				"on_normal_control", "urgent_control" };
		String namesEng[] = { "Highly urgent", "For meeting", "For information", "To participate", "In control",
				"On normal control", "Urgent control" };
		String namesRus[] = { "Весьма срочно", "Для встречи", "Для сведения", "Для участия", "На контроле",
				"На рабочем контроле", "Срочный контроль" };
		String namesKaz[] = { "Жедел бақылауда", "Для встречи", "Для сведения", "Для участия", "На контроле",
				"Жұмыс бақылауында", "Срочный контроль" };
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
