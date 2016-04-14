package staff.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.flabs.localization.Vocabulary;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import staff.dao.RoleDAO;
import staff.model.Role;

/**
 * 
 * 
 * @author Kayra created 08-01-2016
 */

public class FillDefaultRoles extends InitialDataAdapter<Role, RoleDAO> {

	@Override
	public List<Role> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<Role> entities = new ArrayList<Role>();

		/* Common roles */
		Role entity = new Role();
		entity.setName("staff_admin");
		Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Administrator of Staff module");
		name.put(LanguageCode.RUS, "Администратор структуры");
		name.put(LanguageCode.KAZ, "Кұрылымдар администраторі");
		entity.setLocalizedName(name);
		entity.setDescription("the role allow to manage in Staff module");
		entities.add(entity);

		entity = new Role();
		entity.setName("reference_admin");
		name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Administrator of Reference module");
		name.put(LanguageCode.RUS, "Администратор справочников");
		name.put(LanguageCode.KAZ, "Справочник администраторі");
		entity.setLocalizedName(name);
		entity.setDescription("the role allow to manage in Reference module");
		entities.add(entity);

		/* ComProperty application specific roles */
		entity = new Role();
		entity.setName("data_loader");
		name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Data loader");
		name.put(LanguageCode.RUS, "Загрузчик данных");
		name.put(LanguageCode.KAZ, "Деректер тиегіш");
		entity.setLocalizedName(name);
		entity.setDescription("Ответственное лицо по загрузке данных");
		entities.add(entity);

		return entities;
	}

	@Override
	public Class<RoleDAO> getDAO() {
		return RoleDAO.class;
	}

}
