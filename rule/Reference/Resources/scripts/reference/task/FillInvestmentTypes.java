package reference.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.InvestmentTypeDAO;
import reference.model.InvestmentType;

@Command(name = "fill_investment_types")
public class FillInvestmentTypes extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<InvestmentType> entities = new ArrayList<InvestmentType>();
		String[] data = { "Малые предприятия", "Средние предприятия", "Крупные предприятия" };

		String[] dataRus = { "Малые предприятия", "Средние предприятия", "Крупные предприятия" };

		for (int i = 0; i < data.length; i++) {
			InvestmentType entity = new InvestmentType();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, data[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			InvestmentTypeDAO dao = new InvestmentTypeDAO(ses);
			for (InvestmentType entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.info(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				} catch (SecureException e) {
					logger.exception(e);
				}
			}
		} catch (DAOException e) {
			logger.exception(e);
		}
		logger.info("done...");
	}

}
