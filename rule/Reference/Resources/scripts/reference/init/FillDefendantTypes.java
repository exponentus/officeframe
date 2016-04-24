package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.DefendantTypeDAO;
import reference.model.DefendantType;

/**
 * Created by Kayra on 24/04/16.
 */

public class FillDefendantTypes extends InitialDataAdapter<DefendantType, DefendantTypeDAO> {

	@Override
	public List<DefendantType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<DefendantType> entities = new ArrayList<DefendantType>();
		String[] data = { "Общество", "Должностное лицо", "Иные работники", "Физ.лицо" };

		for (int i = 0; i < data.length; i++) {
			DefendantType entity = new DefendantType();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

	@Override
	public Class<DefendantTypeDAO> getDAO() {
		return DefendantTypeDAO.class;
	}

}
