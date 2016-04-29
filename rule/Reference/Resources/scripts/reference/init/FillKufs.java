package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.KufDAO;
import reference.model.Kuf;
import reference.model.constants.KufType;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillKufs extends InitialDataAdapter<Kuf, KufDAO> {

	@Override
	public List<Kuf> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<Kuf> entities = new ArrayList<Kuf>();
		String[] data = { "Не определен", "Мебель", "Животные", "Спортивный инвентарь", "Акции", "Долевое участие", "Прочее движимое имущество",
		        "Школьное оборудование", "Офисное оборудование", "Компьютерное оборудование", "Медицинское оборудование", "Кухонное оборудование",
		        "Оборудование гражданской обороны", "Прочее оборудование", "Здания", "Помещения", "Строения", "Жилые объекты", "Земля",
		        "Памятники истории и культуры", "Транспортные средства", "Легковые автомобили", "Грузовой транспорт", "Специальный транспорт",
		        "Служебный транспорт", "Транспорт медицинской помощи", "Автобус", "Троллейбус", "Трамвай", "Такси", "Водный транспорт",
		        "Специальная техника", "Мотоцикл", "Объект государственного природно-заповедного фонда", "Бомбоубежище", "Завод", "Комбинат",
		        "Аэропорт", "Пешеходный переход", "Билборд", "Электрический столб", "Электрическая сеть", "Тепловая сеть", "Газовая сеть",
		        "Водопровод и канализация", "Арычная сеть", "Дорога", "Паркинг" };
		String[] dataKZ = { "Не определен", "Мебель", "Животные", "Спортивный инвентарь", "Акции", "Долевое участие", "Прочее движимое имущество",
		        "Школьное оборудование", "Офисное оборудование", "Компьютерное оборудование", "Медицинское оборудование", "Кухонное оборудование",
		        "Оборудование гражданской обороны", "Прочее оборудование", "Здания", "Помещения", "Строения", "Жилые объекты", "Земля",
		        "Памятники истории и культуры", "Транспортные средства", "Легковые автомобили", "Грузовой транспорт", "Специальный транспорт",
		        "Служебный транспорт", "Транспорт медицинской помощи", "Автобус", "Троллейбус", "Трамвай", "Такси", "Водный транспорт",
		        "Специальная техника", "Мотоцикл", "Объект государственного природно-заповедного фонда", "Бомбоубежище", "Завод", "Комбинат",
		        "Аэропорт", "Пешеходный переход", "Билборд", "Электрический столб", "Электрическая сеть", "Тепловая сеть", "Газовая сеть",
		        "Водопровод и канализация", "Арычная сеть", "Дорога", "Паркинг" };
		KufType[] code = { KufType.UNKNOWN, KufType.FURNITURE, KufType.ANIMALS, KufType.SPORT_EQUIPMENT, KufType.SHARE_BLOCK, KufType.EQUITY,
		        KufType.OTHERS, KufType.SCHOOL_EQUIPMENT, KufType.OFFICE_EQUIPMENT, KufType.COMPUTER_EQUIPMENT, KufType.MEDICAL_EQUIPMENT,
		        KufType.COOK_EQUIPMENT, KufType.EQUIPMENT_OF_CIVIL_DEFENCE, KufType.OTHERS_EQUIPMENT, KufType.BUILDINGS, KufType.ROOMS,
		        KufType.STRUCTURES, KufType.RESIDENTIAL_OBJECTS, KufType.LAND, KufType.MONUMENT, KufType.AUTOMOBILE, KufType.CAR, KufType.CARGO,
		        KufType.DEJ_TRANSPORT, KufType.OFFICIAL_TRANSPORT, KufType.HOSPITAL_TRANSPORT, KufType.BUS, KufType.TROLLEYBUS, KufType.TRAM,
		        KufType.TAXI, KufType.WATER_TRANSPORT, KufType.SPECIAL_EQUIPMENT, KufType.MOTORCYCLE, KufType.OBJECT_RESERVED_FUND, KufType.BOMBPROOF,
		        KufType.FACTORY, KufType.COMBINES, KufType.AIRPORT, KufType.TRANSITIONS, KufType.BILLBOARD, KufType.COLUMNS,
		        KufType.ELECTRIC_NETWORKS, KufType.THERMAL_NETWORKS, KufType.GAS, KufType.WATER_SYSTEM, KufType.DRAIN, KufType.ROAD,
		        KufType.PARKING };

		for (int i = 0; i < data.length; i++) {
			Kuf entity = new Kuf();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.KAZ, dataKZ[i]);
			name.put(LanguageCode.RUS, data[i]);
			entity.setLocalizedName(name);
			entity.setKuf(code[i]);
			entities.add(entity);
		}

		return entities;
	}

}
