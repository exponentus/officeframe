package reference.init;

import java.util.ArrayList;
import java.util.List;

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

		List<Locality> entities = new ArrayList<Locality>();
		String[] data = { "Капчагай", "Талды-Курган" };
		String[] data1 = { "Алматы" };

		LocalityTypeDAO ltDao = new LocalityTypeDAO(ses);
		RegionDAO cDao = new RegionDAO(ses);
		Region d = cDao.findByName("Алматинская");
		if (d != null) {
			for (String val : data) {
				Locality entity = new Locality();
				entity.setRegion(d);
				entity.setName(val);
				entity.setType(ltDao.findByCode(LocalityCode.CITY));
				entities.add(entity);
			}
		}

		d = cDao.findByName("Алматы");
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

	@Override
	public Class<LocalityDAO> getDAO() {
		return LocalityDAO.class;
	}

}
