package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.BuildingMaterialDAO;
import reference.model.BuildingMaterial;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillBuildingMaterials extends InitialDataAdapter<BuildingMaterial, BuildingMaterialDAO> {

	@Override
	public List<BuildingMaterial> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<BuildingMaterial> entities = new ArrayList<BuildingMaterial>();
		String[] data = { "Кирпич", "Монолитный железобетон", "Железобетонная панель", "Стальной каркас с наполнителем", "Дерево-сруб", "Пеноблок",
		        "Каркасно-камышитовая панель", "Шлакоблок", "Бут", "Саман", "Дерево-шпала", "Метал", "Смешанный" };

		for (int i = 0; i < data.length; i++) {
			BuildingMaterial entity = new BuildingMaterial();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

}
