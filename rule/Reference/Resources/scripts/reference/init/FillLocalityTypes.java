package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.LocalityTypeDAO;
import reference.model.LocalityType;
import reference.model.constants.LocalityCode;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillLocalityTypes extends InitialDataAdapter<LocalityType, LocalityTypeDAO> {

	@Override
	public List<LocalityType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<LocalityType> entities = new ArrayList<LocalityType>();
		String[] dataEng = { "City", "Village" };
		String[] data = { "Город", "Село" };
		String[] dataKZ = { "Город", "Село" };
		LocalityCode[] code = { LocalityCode.CITY, LocalityCode.VILLAGE };

		for (int i = 0; i < data.length; i++) {
			LocalityType entity = new LocalityType();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, dataEng[i]);
			name.put(LanguageCode.KAZ, dataKZ[i]);
			name.put(LanguageCode.RUS, data[i]);
			entity.setLocalizedName(name);
			entity.setCode(code[i]);
			entities.add(entity);
		}

		return entities;
	}

}
