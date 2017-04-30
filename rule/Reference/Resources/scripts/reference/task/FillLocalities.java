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

import reference.dao.LocalityDAO;
import reference.dao.LocalityTypeDAO;
import reference.dao.RegionDAO;
import reference.model.Locality;
import reference.model.Region;
import reference.model.constants.LocalityCode;

@Command(name = "fill_localities")
public class FillLocalities extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Locality> entities = new ArrayList<>();
		String[] data = { "Kapchagay", "Taldykorgan" };
		String[] data1 = { "Almaty" };

		Region d = null;
		LocalityTypeDAO ltDao = null;
		RegionDAO cDao = null;
		try {
			ltDao = new LocalityTypeDAO(ses);
			cDao = new RegionDAO(ses);
			d = cDao.findByName("Almaty region");
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (d != null) {
			for (String val : data) {
				Locality entity = new Locality();
				entity.setRegion(d);
				entity.setName(val);
				entity.setType(ltDao.findByCode(LocalityCode.CITY));
				entities.add(entity);
			}
		}

		try {
			d = cDao.findByName("Almaty");
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (d != null) {
			for (String val : data1) {
				Locality entity = new Locality();
				entity.setRegion(d);
				entity.setName(val);
				entity.setType(ltDao.findByCode(LocalityCode.CITY));
				entities.add(entity);
			}
		}

		try {
			LocalityDAO dao = new LocalityDAO(ses);
			for (Locality entry : entities) {
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
