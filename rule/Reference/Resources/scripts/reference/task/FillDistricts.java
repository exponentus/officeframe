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
		String[] data = { "Karasay", "Talgar" };

		Region region = null;
		try {
			RegionDAO cDao = new RegionDAO(ses);
			region = cDao.findByName("Almaty region");
		} catch (DAOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < data.length; i++) {
			District entity = new District();
			entity.setRegion(region);
			entity.setName(data[i]);
			entities.add(entity);
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
