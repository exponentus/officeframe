package reference.init;

import java.util.ArrayList;
import java.util.List;

import kz.flabs.localization.Vocabulary;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import reference.dao.CityDistrictDAO;
import reference.dao.LocalityDAO;
import reference.model.CityDistrict;
import reference.model.Locality;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillCityDistricts extends InitialDataAdapter<CityDistrict, CityDistrictDAO> {

	@Override
	public List<CityDistrict> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {

		List<CityDistrict> entities = new ArrayList<CityDistrict>();
		String[] data = { "Алатауский", "Алмалинский", "Ауэзовский", "Бостандыкский", "Жетысуский", "Медеуский", "Наурызбайский", "Турксибский" };

		LocalityDAO cDao = new LocalityDAO(ses);
		Locality region = cDao.findByName("Алматы");

		for (int i = 0; i < data.length; i++) {
			CityDistrict entity = new CityDistrict();
			entity.setLocality(region);
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;

	}

	@Override
	public Class<CityDistrictDAO> getDAO() {
		return CityDistrictDAO.class;
	}

}
