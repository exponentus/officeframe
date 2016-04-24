package reference.init;

import java.util.ArrayList;
import java.util.List;

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
		List<DepartmentType> entities = new ArrayList<DepartmentType>();
		String[] data = { "Отдел", "Сектор" };

		for (int i = 0; i < data.length; i++) {
			DepartmentType entity = new DepartmentType();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

	@Override
	public Class<DepartmentTypeDAO> getDAO() {
		return DepartmentTypeDAO.class;
	}

}
