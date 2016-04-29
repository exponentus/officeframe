package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.LawArticleDAO;
import reference.model.LawArticle;

/**
 * Created by Kayra on 24/04/16.
 */

public class FillLawArticles extends InitialDataAdapter<LawArticle, LawArticleDAO> {

	@Override
	public List<LawArticle> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<LawArticle> entities = new ArrayList<LawArticle>();
		String[] data = { "" };

		for (int i = 0; i < data.length; i++) {
			LawArticle entity = new LawArticle();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

}
