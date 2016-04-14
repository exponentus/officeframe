package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.flabs.localization.Vocabulary;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import reference.dao.RegionTypeDAO;
import reference.model.RegionType;
import reference.model.constants.RegionCode;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillRegionTypes extends InitialDataAdapter<RegionType, RegionTypeDAO> {

	@Override
	public List<RegionType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<RegionType> entities = new ArrayList<RegionType>();
		String[] dataEng = { "Federation", "Region", "Urban agglomeration" };
		String[] data = { "Федерация", "Область", "Городская агломерация" };
		String[] dataKZ = { "Федерация", "Область", "Городская агломерация" };
		RegionCode[] code = { RegionCode.FEDERATION, RegionCode.REGION, RegionCode.URBAN_AGGLOMERATION };

		for (int i = 0; i < data.length; i++) {
			RegionType entity = new RegionType();
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

	@Override
	public Class<RegionTypeDAO> getDAO() {
		return RegionTypeDAO.class;
	}

}
