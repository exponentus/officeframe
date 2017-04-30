package reference.task;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.CityDistrictDAO;
import reference.dao.LocalityDAO;
import reference.model.CityDistrict;
import reference.model.Locality;

@Command(name = "fill_city_districts")
public class FillCityDistricts extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {

		List<CityDistrict> entities = new ArrayList<>();
		String[] data = { "Алатауский", "Алмалинский", "Ауэзовский", "Бостандыкский", "Жетысуский", "Медеуский",

				"Наурызбайский", "Турксибский" };
		Locality region = null;
		try {
			LocalityDAO cDao = new LocalityDAO(ses);
			region = cDao.findByName("Almaty");
			if (region != null) {
				for (int i = 0; i < data.length; i++) {
					CityDistrict entity = new CityDistrict();
					entity.setLocality(region);
					entity.setName(data[i]);
					entities.add(entity);
				}
				try {
					CityDistrictDAO dao = new CityDistrictDAO(ses);
					for (CityDistrict entry : entities) {
						try {
							if (dao.add(entry) != null) {
								logger.info(entry.getName() + " added");
							}
						} catch (DAOException e) {
							if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
								logger.warning(
										"a data is already exists (" + e.getAddInfo() + "), record was skipped");
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
			} else {
				logger.error("Locality has not been found");
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		logger.info("done...");
	}

}
