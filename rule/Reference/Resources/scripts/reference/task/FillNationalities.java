package reference.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.NationalityDAO;
import reference.model.Nationality;

@Command(name = "fill_nationalities")
public class FillNationalities extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Nationality> entities = new ArrayList<Nationality>();
		String[] data = { "kazakh", "russians", "ukraininan", "germans", "tatars", "others" };

		String[] dataRus = { "Казахи", "Русские", "Украинцы", "Немцы", "Татары", "Другие" };

		for (int i = 0; i < data.length; i++) {
			Nationality entity = new Nationality();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, WordUtils.capitalize(data[i]));
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			NationalityDAO dao = new NationalityDAO(ses);
			for (Nationality entry : entities) {
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
