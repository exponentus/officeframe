package reference.task;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
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
		String[] pavlodarDistricts = { "Актогайский", "Баянаульский", "Железинский", "Иртышский", "Качирский", "Лебяжинский", "Майский",
				"Павлодарский", "Успенский", "Щербактинский" };

		Region region = null;
		RegionDAO cDao = null;
		try {
			cDao = new RegionDAO(ses);

			region = cDao.findByName("Almaty region");
			for (int i = 0; i < almatyDistricts.length; i++) {
				District entity = new District();
				entity.setRegion(region);
				entity.setName(almatyDistricts[i]);
				entities.add(entity);
			}

			region = cDao.findByName("Pavlodar region");
			for (int i = 0; i < pavlodarDistricts.length; i++) {
				District entity = new District();
				entity.setRegion(region);
				entity.setName(pavlodarDistricts[i]);
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
