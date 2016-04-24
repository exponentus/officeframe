package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;

import kz.flabs.localization.Vocabulary;
import reference.dao.ResponsibleTypeDAO;
import reference.model.ResponsibleType;

/**
 * Created by Kayra on 24/04/16.
 */

public class FillResponsibleTypes extends InitialDataAdapter<ResponsibleType, ResponsibleTypeDAO> {

	@Override
	public List<ResponsibleType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<ResponsibleType> entities = new ArrayList<ResponsibleType>();
		String[] data = { "Исполнитель", "Адвокат-Физическое лиц", "Консалтинговая организация/адвокатская контора", "Иное" };

		for (int i = 0; i < data.length; i++) {
			ResponsibleType entity = new ResponsibleType();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

	@Override
	public Class<ResponsibleTypeDAO> getDAO() {
		return ResponsibleTypeDAO.class;
	}

}
