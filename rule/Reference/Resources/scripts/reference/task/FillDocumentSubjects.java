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
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.DocumentSubjectDAO;
import reference.model.DocumentSubject;

@Command(name = "fill_document_subjs")
public class FillDocumentSubjects extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<DocumentSubject> entities = new ArrayList<>();

		String names[] = { "legal", "production" };
		String namesEng[] = { "Legal", "Production" };
		String namesRus[] = { "Правового", "Производственный" };
		for (int i = 0; i < names.length; i++) {
			DocumentSubject dType = new DocumentSubject();
			dType.setName(names[i]);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.RUS, namesRus[i]);
			name.put(LanguageCode.ENG, namesEng[i]);
			dType.setLocName(name);
			entities.add(dType);
		}

		try {
			DocumentSubjectDAO dao = new DocumentSubjectDAO(ses);
			for (DocumentSubject entry : entities) {
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
