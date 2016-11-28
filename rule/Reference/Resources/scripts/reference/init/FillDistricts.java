package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.DistrictDAO;
import reference.dao.RegionDAO;
import reference.model.District;
import reference.model.Region;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillDistricts extends InitialDataAdapter<District, DistrictDAO> {
	
	@Override
	public List<District> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		
		List<District> entities = new ArrayList<>();
		String[] data = { "Karasay", "Talgar" };
		
		Region region = null;
		try {
			RegionDAO cDao = new RegionDAO(ses);
			region = cDao.findByName("Almaty region");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < data.length; i++) {
			District entity = new District();
			entity.setRegion(region);
			entity.setName(data[i]);
			entities.add(entity);
		}
		
		return entities;
		
	}
	
}
