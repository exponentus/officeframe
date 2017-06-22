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
import reference.model.IndustryTypeCategory;

@Command(name = "fill_industry_type_categories")
public class FillIndustryTypeCategories extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<IndustryTypeCategory> entities = new ArrayList<IndustryTypeCategory>();
		String[] data = { "Сельское, лесное и рыбное хозяйство", "Промышленность", "Строительство",
				"Оптовая и розничная торговля; ремонт автомобилей и мотоциклов", "Транспорт и складирование",
				"Услуги по проживанию и питанию", "Информация и связь", "Финансовая и страховая деятельность",
				"Операции с недвижимым имуществом", "Профессиональная, научная и техническая деятельность",
				"Деятельность в области административного и вспомогательного обслуживания",
				"Государственное управление и оборона; обязательное социальное обеспечение", "Образование",
				"Здравоохранение и социальные услуги", "Искусство, развлечения и отдых", "Предоставление прочих видов услуг",
				"Налоги на продукты" };

		String[] dataRus = { "Сельское, лесное и рыбное хозяйство", "Промышленность", "Строительство",
				"Оптовая и розничная торговля; ремонт автомобилей и мотоциклов", "Транспорт и складирование",
				"Услуги по проживанию и питанию", "Информация и связь", "Финансовая и страховая деятельность",
				"Операции с недвижимым имуществом", "Профессиональная, научная и техническая деятельность",
				"Деятельность в области административного и вспомогательного обслуживания",
				"Государственное управление и оборона; обязательное социальное обеспечение", "Образование",
				"Здравоохранение и социальные услуги", "Искусство, развлечения и отдых", "Предоставление прочих видов услуг",
				"Налоги на продукты" };

		for (int i = 0; i < data.length; i++) {
			IndustryTypeCategory entity = new IndustryTypeCategory();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, data[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			IndustryTypeCategoryDAO dao = new IndustryTypeCategoryDAO(ses);
			for (IndustryTypeCategory entry : entities) {
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
