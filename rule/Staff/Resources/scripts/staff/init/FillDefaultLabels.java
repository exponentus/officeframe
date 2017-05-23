package staff.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.Vocabulary;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;

import staff.dao.OrganizationLabelDAO;
import staff.model.OrganizationLabel;

/**
 *
 *
 * @author Kayra created 09-01-2016
 */

public class FillDefaultLabels extends InitialDataAdapter<OrganizationLabel, OrganizationLabelDAO> {

	@Override
	public List<OrganizationLabel> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<OrganizationLabel> entities = new ArrayList<>();

		OrganizationLabel entity = new OrganizationLabel();
		entity.setName("inactive");
		Map<LanguageCode, String> name = new HashMap<>();
		name.put(LanguageCode.ENG, "Inactive organization");
		name.put(LanguageCode.RUS, "Не действующая организация");
		name.put(LanguageCode.KAZ, "Ұйымдастыру міндетін атқарушы емес");
		entity.setLocName(name);
		entities.add(entity);

		entity = new OrganizationLabel();
		entity.setName("primary");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Primary organization");
		name.put(LanguageCode.RUS, "Первичная организация");
		name.put(LanguageCode.KAZ, "Бастауыш ұйымы");
		entity.setLocName(name);
		entities.add(entity);

		/* ComProperty application specific labels */
		entity = new OrganizationLabel();
		entity.setName("balance_holder");
		name = new HashMap<>();
		name.put(LanguageCode.ENG, "Balance holder");
		name.put(LanguageCode.RUS, "Организация-балансодержатель");
		name.put(LanguageCode.KAZ, "Организация-балансодержатель");
		entity.setLocName(name);
		// entities.add(entity);

		return entities;
	}

	@Override
	public Class<OrganizationLabelDAO> getDAO() {
		return OrganizationLabelDAO.class;
	}

}
