package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.LocalityDAO;
import reference.dao.LocalityTypeDAO;
import reference.dao.RegionDAO;
import reference.model.Locality;
import reference.model.Region;
import reference.model.constants.LocalityCode;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillLocalities extends InitialDataAdapter<Locality, LocalityDAO> {

	@Override
	public List<Locality> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {

		List<Locality> entities = new ArrayList<>();
		String[] data = { "Kapchagay", "Taldykorgan" };
		String[] data1 = { "Almaty" };

		LocalityTypeDAO ltDao = new LocalityTypeDAO(ses);
		RegionDAO cDao = new RegionDAO(ses);
		Region d = null;
		try {
			d = cDao.findByName("Almaty region");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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

		return entities;

	}

}
