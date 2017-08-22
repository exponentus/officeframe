package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.BuildingMaterialDAO;
import reference.model.BuildingMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "fill_building_materials")
public class FillBuildingMaterials extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<BuildingMaterial> entities = new ArrayList<>();

		String names[] = { "brick", "monolithic reinforced concrete", "reinforced concrete panel", "steel frame with filler",
				"wood-frame", "foamblock", "skeleton-reed panel", "slagblock", "rubble", "clay", "wood-sleepers",
				"metal", "mixed" };
		String namesRus[] = { "Кирпич", "Монолитный железобетон", "Железобетонная панель", "Стальной каркас с наполнителем",
			"Дерево-сруб", "Пеноблок","Каркасно-камышитовая панель", "Шлакоблок", "Бут", "Саман", "Дерево-шпала",
			"Метал", "Смешанный" };
		String namesKaz[] = { "Кирпич", "Монолитный железобетон", "Железобетонная панель", "Стальной каркас с наполнителем",
				"Дерево-сруб", "Пеноблок","Каркасно-камышитовая панель", "Шлакоблок", "Бут", "Саман", "Дерево-шпала",
				"Метал", "Смешанный" };
		for (int i = 0; i < names.length; i++) {
			BuildingMaterial dType = new BuildingMaterial();
			dType.setName(names[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.RUS, namesRus[i]);
			name.put(LanguageCode.KAZ, namesKaz[i]);
			dType.setLocName(name);
			entities.add(dType);
		}

		try {
			BuildingMaterialDAO dao = new BuildingMaterialDAO(ses);
			for (BuildingMaterial entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.info(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				} catch (SecureException e) {
					logger.exception(e);
				}
			}
		} catch (DAOException e) {
			logger.exception(e);
		}
		logger.info("done...");
	}

}
