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

import reference.dao.IndustryTypeCategoryDAO;
import reference.dao.IndustryTypeDAO;
import reference.model.IndustryType;
import reference.model.IndustryTypeCategory;

@Command(name = "fill_industry_types")
public class FillIndustryTypes extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<IndustryType> entities = new ArrayList<IndustryType>();
		String[] data = { "горнодобывающая промышленность и разработка карьеров", "обрабатывающая промышленность",
				"электроснабжение, подача газа, пара и воздушное кондиционирование",
				"водоснабжение; канализационная система, контроль над сбором и распределением отходов" };

		String[] dataRus = { "горнодобывающая промышленность и разработка карьеров", "обрабатывающая промышленность",
				"электроснабжение, подача газа, пара и воздушное кондиционирование",
				"водоснабжение; канализационная система, контроль над сбором и распределением отходов" };

		try {

			IndustryTypeCategory cat = new IndustryTypeCategoryDAO(ses).findByName("Промышленность");

			for (int i = 0; i < data.length; i++) {
				IndustryType entity = new IndustryType();
				entity.setName(data[i]);
				Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
				name.put(LanguageCode.ENG, data[i]);
				name.put(LanguageCode.RUS, dataRus[i]);
				entity.setLocName(name);
				entity.setCategory(cat);
				entities.add(entity);
			}

			IndustryTypeDAO dao = new IndustryTypeDAO(ses);
			for (IndustryType entry : entities) {
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
