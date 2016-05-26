package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.UnitTypeDAO;
import reference.model.CategoryOfUnit;

/**
 * @author Kayra on 26/03/16.
 */

public class FillUnitType extends InitialDataAdapter<CategoryOfUnit, UnitTypeDAO> {

	@Override
	public List<CategoryOfUnit> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<CategoryOfUnit> entities = new ArrayList<CategoryOfUnit>();
		String[] data = { "Мебель", "Орг.техника", "Канцелярские принадлежности" };

		for (int i = 0; i < data.length; i++) {
			CategoryOfUnit entity = new CategoryOfUnit();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

}
