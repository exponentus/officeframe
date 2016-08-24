package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;

/**
 * @author Kayra on 17/22/16.
 */

public class FillOrgCategories extends InitialDataAdapter<OrgCategory, OrgCategoryDAO> {

	@Override
	public List<OrgCategory> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<OrgCategory> entities = new ArrayList<OrgCategory>();
		String[] data = { "LTD", "Self employed", "JSC", "State office" };
		String[] dataRus = { "ТОО", "Частный предприниматель", "АО", "Государственное ведомство" };

		for (int i = 0; i < data.length; i++) {
			OrgCategory entity = new OrgCategory();
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, data[i]);
			name.put(LanguageCode.KAZ, dataRus[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocalizedName(name);
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

}
