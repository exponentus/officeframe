package staff.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import staff.dao.OrganizationLabelDAO;
import staff.model.OrganizationLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "fill_organization_labels")
public class FillOrganizationLabels extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<OrganizationLabel> entities = new ArrayList<OrganizationLabel>();
		String[] data = { "inactive organization","primary organization" };
		String[] dataEng = { "Inactive organization","Primary organization"};
		String[] dataPor = { "Organização inativa","Organização primária"};
		String[] dataRus = { "Не действующая организация","Первичная организация"};
		String[] dataKaz = { "Ұйымдастыру міндетін атқарушы емес","Бастауыш ұйымы"};

		for (int i = 0; i < data.length; i++) {
			OrganizationLabel entity = new OrganizationLabel();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, dataEng[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			name.put(LanguageCode.POR, dataPor[i]);
			name.put(LanguageCode.KAZ, dataKaz[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			OrganizationLabelDAO dao = new OrganizationLabelDAO(ses);
			for (OrganizationLabel entry : entities) {
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
