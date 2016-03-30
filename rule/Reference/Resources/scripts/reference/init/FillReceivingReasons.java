package reference.init;

import java.util.ArrayList;
import java.util.List;

import kz.flabs.localization.Vocabulary;
import kz.lof.dataengine.jpa.deploying.InitialDataAdapter;
import kz.lof.localization.LanguageCode;
import kz.lof.scripting._Session;
import reference.dao.ReceivingReasonDAO;
import reference.model.ReceivingReason;

/**
 * 
 * 
 * @author Kayra created 07-01-2016
 */
public class FillReceivingReasons extends InitialDataAdapter<ReceivingReason, ReceivingReasonDAO> {

	@Override
	public List<ReceivingReason> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		List<ReceivingReason> entities = new ArrayList<ReceivingReason>();
		String[] data = { "Приобретено", "Принято из республиканской собственности", "Принято из коммунальной собственности", "Другое" };

		for (int i = 0; i < data.length; i++) {
			ReceivingReason entity = new ReceivingReason();
			entity.setName(data[i]);
			entities.add(entity);
		}

		return entities;
	}

	@Override
	public Class<ReceivingReasonDAO> getDAO() {
		return ReceivingReasonDAO.class;
	}

}
