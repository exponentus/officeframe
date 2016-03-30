package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.flabs.localization.Vocabulary;
import kz.lof.dataengine.jpa.deploying.InitialDataAdapter;
import kz.lof.localization.LanguageCode;
import kz.lof.scripting._Session;
import reference.dao.TagDAO;
import reference.model.Tag;

/**
 * Created by Kayra on 28/01/16.
 */

public class FillTags extends InitialDataAdapter<Tag, TagDAO> {

	@Override
	public List<Tag> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<Tag> entities = new ArrayList<Tag>();

		Tag entity = new Tag();
		entity.setName("starred");
		Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Starred");
		name.put(LanguageCode.RUS, "Избранный");
		name.put(LanguageCode.KAZ, "Сүйікті");
		entity.setLocalizedName(name);
		entities.add(entity);

		return entities;
	}

	@Override
	public Class<TagDAO> getDAO() {
		return TagDAO.class;
	}

}
