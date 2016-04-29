package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;

import reference.dao.ClaimantDecisionTypeDAO;
import reference.model.ClaimantDecisionType;

/**
 * Created by Kayra on 24/04/16.
 */

public class FillClaimDecisionTypes extends InitialDataAdapter<ClaimantDecisionType, ClaimantDecisionTypeDAO> {

	@Override
	public List<ClaimantDecisionType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<ClaimantDecisionType> entities = new ArrayList<ClaimantDecisionType>();
		String[] data = { "О возбуждении уголовного дела", "Об отказе уголовного дела",
		        "О передаче заявленияр сообщения по подследственности, а по делам частного обвинения - по подсудности",
		        "Об осуществлении упрощенного досудебного производства в порядке, предусмотренном главой 23-1 настоящего кодекса" };

		for (int i = 0; i < data.length; i++) {
			ClaimantDecisionType entity = new ClaimantDecisionType();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

}
