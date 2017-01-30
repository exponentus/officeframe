package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;

/**
 * @author Kayra on 26/03/16.
 */

public class FillDepartmentType extends InitialDataAdapter<DepartmentType, DepartmentTypeDAO> {
	
	@Override
	public List<DepartmentType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<DepartmentType> entities = new ArrayList<>();
		String[] data = { "Department", "Sector", "Group", "Management" };
		String[] dataRus = { "Департамент", "Сектор", "Группа", "Управление" };
		String[] dataKaz = { "Департамент", "Сектор", "Группа", "Управление" };
		
		for (int i = 0; i < data.length; i++) {
			DepartmentType entity = new DepartmentType();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.ENG, data[i]);
			name.put(LanguageCode.KAZ, dataKaz[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entities.add(entity);
		}
		
		return entities;
	}
	
}
