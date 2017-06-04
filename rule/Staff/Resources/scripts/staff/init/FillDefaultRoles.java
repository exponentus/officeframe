package staff.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.Vocabulary;
import com.exponentus.localization.constants.LanguageCode;
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
		List<Role> entities = new ArrayList<>();

		/* Common roles */
		Role entity = new Role();
		entity.setName("staff_admin");
		Map<LanguageCode, String> name = new HashMap<>();
		name.put(LanguageCode.ENG, "Administrator of Staff module");
		name.put(LanguageCode.RUS, "Администратор структуры");
		name.put(LanguageCode.KAZ, "Кұрылымдар администраторі");
		entity.setLocName(name);
		entities.add(entity);

		entity = new Role();
		entity.setName("reference_admin");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Administrator of Reference module");
		name.put(LanguageCode.RUS, "Администратор справочников");
		name.put(LanguageCode.KAZ, "Справочник администраторі");
		entity.setLocName(name);
		entities.add(entity);

		entity = new Role();
		entity.setName("senior_manager");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Senior Manager");
		name.put(LanguageCode.RUS, "Руководитель высшего звена");
		name.put(LanguageCode.KAZ, "Бас");
		entity.setLocName(name);
		entities.add(entity);

		entity = new Role();
		entity.setName("can_sign_outgoing");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "It has the right to sign");
		name.put(LanguageCode.RUS, "Имеет право подписи");
		name.put(LanguageCode.KAZ, "Имеет право подписи");
		entity.setLocName(name);
		entities.add(entity);

		entity = new Role();
		entity.setName("fired");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Fired");
		name.put(LanguageCode.RUS, "Уволен");
		name.put(LanguageCode.KAZ, "Босатылған");
		entity.setLocName(name);
		entities.add(entity);

		entity = new Role();
		entity.setName("chancellery");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Chancellery");
		name.put(LanguageCode.RUS, "Канцелярия");
		name.put(LanguageCode.KAZ, "Кенсе");
		entity.setLocName(name);
		entities.add(entity);

		/* ComProperty application specific roles */
		entity = new Role();
		entity.setName("data_loader");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Data loader");
		name.put(LanguageCode.RUS, "Загрузчик данных");
		name.put(LanguageCode.KAZ, "Деректер тиегіш");
		entity.setLocName(name);
		// entities.add(entity);

		return entities;
	}

}
