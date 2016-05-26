package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.UnitTypeDAO;
import reference.model.UnitType;

/**
 * @author Kayra on 26/03/16.
 */

public class FillUnitType extends InitialDataAdapter<UnitType, UnitTypeDAO> {

	@Override
	public List<UnitType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<UnitType> entities = new ArrayList<UnitType>();
		String[] data = { "Мебель", "Орг.техника", "Канцелярские принадлежности" };

		for (int i = 0; i < data.length; i++) {
			UnitType entity = new UnitType();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

}
