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

import reference.dao.CountryDAO;
import reference.dao.RegionDAO;
import reference.dao.RegionTypeDAO;
import reference.model.Country;
import reference.model.Region;
import reference.model.RegionType;
import reference.model.constants.RegionCode;

@Command(name = "fill_regions")
public class FillRegions extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Region> entities = new ArrayList<>();
		String[] data = { "Almaty", "Astana", "Almaty region" };
		try {
			CountryDAO cDao = new CountryDAO(ses);
			Country country = null;

			country = cDao.findByName("Kazakhstan");

			for (int i = 0; i < data.length; i++) {
				Region entity = new Region();
				entity.setCountry(country);
				entity.setName(data[i]);
				RegionTypeDAO rtDao = new RegionTypeDAO(ses);
				RegionType rType = null;
				if (data[i].equals("Almaty") || data[i].equals("Astana")) {
					rType = rtDao.findByCode(RegionCode.URBAN_AGGLOMERATION);
				} else {
					rType = rtDao.findByCode(RegionCode.REGION);
				}
				entity.setType(rType);
				entities.add(entity);
			}

			String[] data1 = { "Moscow", "Saint-Petersburg" };
			Country country1 = null;

			country1 = cDao.findByName("Russia");

			for (int i = 0; i < data1.length; i++) {
				Region entity = new Region();
				entity.setCountry(country1);
				entity.setName(data1[i]);
				RegionTypeDAO rtDao = new RegionTypeDAO(ses);
				RegionType rType = null;
				if (data1[i].equals("Moscow") || data1[i].equals("Saint-Petersburg")) {
					rType = rtDao.findByCode(RegionCode.URBAN_AGGLOMERATION);
				} else {
					rType = rtDao.findByCode(RegionCode.REGION);
				}
				entity.setType(rType);
				entities.add(entity);
			}

			String[] data2 = { "Lisbon", "Leiria" };
			Country country2 = null;

			country2 = cDao.findByName("Portugal");

			for (int i = 0; i < data2.length; i++) {
				Region entity = new Region();
				entity.setCountry(country2);
				entity.setName(data2[i]);
				RegionTypeDAO rtDao = new RegionTypeDAO(ses);
				RegionType rType = null;
				if (data2[i].equals("Lisbon")) {
					rType = rtDao.findByCode(RegionCode.URBAN_AGGLOMERATION);
				} else {
					rType = rtDao.findByCode(RegionCode.REGION);
				}
				entity.setType(rType);
				entities.add(entity);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		try {
			RegionDAO dao = new RegionDAO(ses);
			for (Region entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.infoLogEntry(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				} catch (SecureException e) {
					logger.errorLogEntry(e);
				}
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
		}
		logger.infoLogEntry("done...");
	}

}
