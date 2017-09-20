package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.DocumentSubjectDAO;
import reference.dao.RealEstateObjStatusDAO;
import reference.init.AppConst;
import reference.model.DocumentSubject;
import reference.model.RealEstateObjStatus;
import reference.model.constants.CountryCode;
import reference.model.constants.RealEstateObjStatusCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_real_estate_obj_statuses")
public class FillRealEstateObjStatus extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<RealEstateObjStatus> entities = new ArrayList<>();

		String names[] = { "on_sale", "owned", "rental", "rented" };
		String namesEng[] = { "On sale", "Owned","Rental", "Rented" };
		String namesRus[] = { "На продажу", "В собственности","Сдается в аренду", "В аренде"};
		RealEstateObjStatusCode[] code = {RealEstateObjStatusCode.ON_SALE, RealEstateObjStatusCode.OWNED,
				RealEstateObjStatusCode.RENTAL, RealEstateObjStatusCode.RENTED};

		for (int i = 0; i < names.length; i++) {
			RealEstateObjStatus status = new RealEstateObjStatus();
			status.setName(names[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.RUS, namesRus[i]);
			name.put(LanguageCode.ENG, namesEng[i]);
			name.put(LanguageCode.KAZ, names[i]);
			status.setLocName(name);
			status.setCode(code[i]);
			entities.add(status);
		}

		try {
			RealEstateObjStatusDAO dao = new RealEstateObjStatusDAO(ses);
			for (RealEstateObjStatus entry : entities) {
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
