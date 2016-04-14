package staff.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.flabs.localization.Vocabulary;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
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
		List<OrganizationLabel> entities = new ArrayList<OrganizationLabel>();

		OrganizationLabel entity = new OrganizationLabel();
		entity.setName("inactive");
		Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Inactive organization");
		name.put(LanguageCode.RUS, "Не действующая организация");
		name.put(LanguageCode.KAZ, "Ұйымдастыру міндетін атқарушы емес");
		entity.setLocalizedName(name);
		entity.setDescription("Inactive organization");
		entities.add(entity);

		entity = new OrganizationLabel();
		entity.setName("primary");
		name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Primary organization");
		name.put(LanguageCode.RUS, "Первичная организация");
		name.put(LanguageCode.KAZ, "Бастауыш ұйымы");
		entity.setLocalizedName(name);
		entity.setDescription("primary organization");
		entities.add(entity);

		entity = new OrganizationLabel();
		entity.setName("branch");
		name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Branch");
		name.put(LanguageCode.RUS, "Филиал");
		name.put(LanguageCode.KAZ, "Филиал");
		entity.setLocalizedName(name);
		entity.setDescription("organization is a branch of the primary organization");
		entities.add(entity);

		/* ComProperty application specific labels */
		entity = new OrganizationLabel();
		entity.setName("balance_holder");
		name = new HashMap<LanguageCode, String>();
		name.put(LanguageCode.ENG, "Balance holder");
		name.put(LanguageCode.RUS, "Организация-балансодержатель");
		name.put(LanguageCode.KAZ, "Организация-балансодержатель");
		entity.setLocalizedName(name);
		entity.setDescription("Организация-балансодержатель");
		entities.add(entity);

		return entities;
	}

	@Override
	public Class<OrganizationLabelDAO> getDAO() {
		return OrganizationLabelDAO.class;
	}

}
