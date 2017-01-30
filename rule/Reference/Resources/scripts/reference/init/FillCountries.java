package reference.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.CountryDAO;
import reference.model.Country;
import reference.model.constants.CountryCode;

/**
 * Created by Kayra on 30/12/15.
 */

public class FillCountries extends InitialDataAdapter<Country, CountryDAO> {
	
	@Override
	public List<Country> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<Country> entities = new ArrayList<Country>();
		String[] data = { "Kazakhstan", "Russia", "Byelorussia", "Ukraine", "Germany", "France", "Turkey", "USA",
				"China", "Bulgaria", "Portugal" };
		String[] dataRus = { "Казахстан", "Россия", "Беларуссия", "Украина", "Германия", "Франция", "Турция", "США",
				"Китай", "Болгария", "Португалия" };
		String[] dataKaz = { "Казахстан", "Россия", "Беларуссия", "Украина", "Германия", "Франция", "Турция", "США",
				"Китай", "Болгария", "Португалия" };
		String[] dataEng = { "Kazakhstan", "Russia", "Byelorussia", "Ukraine", "Germany", "France", "Turkey", "USA",
				"China", "Bulgaria", "Portugal" };
		CountryCode[] code = { CountryCode.KZ, CountryCode.RU, CountryCode.BY, CountryCode.UA, CountryCode.DE,
				CountryCode.FR, CountryCode.TR, CountryCode.US, CountryCode.CN, CountryCode.BG, CountryCode.PT };
		
		for (int i = 0; i < data.length; i++) {
			Country entity = new Country();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, dataEng[i]);
			name.put(LanguageCode.KAZ, dataKaz[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entity.setCode(code[i]);
			entities.add(entity);
		}
		
		return entities;
	}
	
}
