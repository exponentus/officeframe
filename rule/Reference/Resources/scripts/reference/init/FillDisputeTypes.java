package reference.init;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;

import kz.flabs.localization.Vocabulary;
import reference.dao.DisputeTypeDAO;
import reference.model.DisputeType;

/**
 * Created by Kayra on 24/04/16.
 */

public class FillDisputeTypes extends InitialDataAdapter<DisputeType, DisputeTypeDAO> {

	@Override
	public List<DisputeType> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<DisputeType> entities = new ArrayList<DisputeType>();
		String[] data = { "Имущественного характера", "Не имущественного характера" };

		for (int i = 0; i < data.length; i++) {
			DisputeType entity = new DisputeType();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

	@Override
	public Class<DisputeTypeDAO> getDAO() {
		return DisputeTypeDAO.class;
	}

}
