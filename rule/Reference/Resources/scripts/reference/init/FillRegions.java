package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.CountryDAO;
import reference.dao.RegionDAO;
import reference.dao.RegionTypeDAO;
import reference.model.Country;
import reference.model.Region;
import reference.model.RegionType;
import reference.model.constants.RegionCode;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillRegions extends InitialDataAdapter<Region, RegionDAO> {
	
	@Override
	public List<Region> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entities;
		
	}
	
}
