package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.LawBranchDAO;
import reference.model.LawBranch;

/**
 * Created by Kayra on 24/04/16.
 */

public class FillLawBranches extends InitialDataAdapter<LawBranch, LawBranchDAO> {

	@Override
	public List<LawBranch> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<LawBranch> entities = new ArrayList<LawBranch>();
		String[] data = { "Налоговое", "Трудовое", "Земельное", "Экологическое", "Таможенное", "Недропользование", "Закупки", "Иное" };

		for (int i = 0; i < data.length; i++) {
			LawBranch entity = new LawBranch();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

	@Override
	public Class<LawBranchDAO> getDAO() {
		return LawBranchDAO.class;
	}

}
