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

import reference.dao.DistrictDAO;
import reference.dao.RegionDAO;
import reference.model.District;
import reference.model.Region;

@Command(name = "fill_districts")
public class FillDistricts extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<District> entities = new ArrayList<>();
		String[] almatyDistricts = { "Karasay", "Talgar" };
		String[] pavlodarDistricts = { "Aktogaysky", "Bayanaulsky", "Zhelesinsky", "Irtishsky", "Cachirsky", "Lebyazhynsky", "Maysky",
				"Pavlodarsky", "Uspensky", "Sherbaktinsky" };
		String[] pavlodarDistrictsRus = { "Актогайский", "Баянаульский", "Железинский", "Иртышский", "Качирский", "Лебяжинский", "Майский",
				"Павлодарский", "Успенский", "Щербактинский" };

		String[] zhambylDistricts = { "talasky", "shusky", "zhambulsky", "kordaysky", "zhualynsky", "sarysusky", "ryskulovsky",
				"moinkumsky", "baizaksky" };
		String[] zhambylDistrictsEng = { "Talasky", "Shusky", "Zhambulsky", "Kordaysky", "Zhualynsky", "Sarysusky", "Ryskulovsky",
				"Moinkumsky", "Baizaksky" };
		String[] zhambylDistrictsRus = { "Талаский", "Шуский", "Жамбылский", "Кордайский", "Жуалынский", "Сарысуский", "Т.Рыскуловский",
				"Моинкумский", "Байзакский" };

		Region region = null;
		RegionDAO cDao = null;
		try {
			cDao = new RegionDAO(ses);

			region = cDao.findByName("almaty_region");
			for (int i = 0; i < almatyDistricts.length; i++) {
				District entity = new District();
				entity.setRegion(region);
				entity.setName(almatyDistricts[i]);
				entities.add(entity);
			}

			region = cDao.findByName("pavlodar_region");
			for (int i = 0; i < pavlodarDistricts.length; i++) {
				District entity = new District();
				entity.setRegion(region);
				entity.setName(pavlodarDistricts[i]);
				Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
				name.put(LanguageCode.ENG, pavlodarDistricts[i]);
				name.put(LanguageCode.RUS, pavlodarDistrictsRus[i]);
				name.put(LanguageCode.KAZ, pavlodarDistrictsRus[i]);
				entity.setLocName(name);
				entities.add(entity);
			}

			region = cDao.findByName("zhambyl_region");
			for (int i = 0; i < zhambylDistricts.length; i++) {
				District entity = new District();
				entity.setRegion(region);
				entity.setName(zhambylDistricts[i]);
				Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
				name.put(LanguageCode.ENG, zhambylDistrictsEng[i]);
				name.put(LanguageCode.RUS, zhambylDistrictsRus[i]);
				name.put(LanguageCode.KAZ, zhambylDistrictsRus[i]);
				entity.setLocName(name);
				entities.add(entity);
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}

		try {
			DistrictDAO dao = new DistrictDAO(ses);
			for (District entry : entities) {
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
