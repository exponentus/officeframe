package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.TagDAO;
import reference.model.Tag;

/**
 * Created by Kayra on 28/01/16.
 */

public class FillTags extends InitialDataAdapter<Tag, TagDAO> {

	@Override
	public List<Tag> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<Tag> entities = new ArrayList<>();

		Tag entity = new Tag();
		entity.setName("starred");
		Map<LanguageCode, String> name = new HashMap<>();
		name.put(LanguageCode.ENG, "Starred");
		name.put(LanguageCode.RUS, "Избранный");
		name.put(LanguageCode.KAZ, "Сүйікті");
		entity.setLocalizedName(name);
		entity.setColor("#d94600");
		entities.add(entity);

		entity = new Tag();
		entity.setName("expired");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Overdued");
		name.put(LanguageCode.RUS, "Просроченный");
		name.put(LanguageCode.KAZ, "Mерзімі өткен");
		entity.setLocalizedName(name);
		entity.setColor("#db0000");
		entity.setHidden(true);
		entities.add(entity);

		return entities;
	}

}
