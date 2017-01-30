package reference.task;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import administrator.init.ServerConst;
import administrator.model.Language;
import reference.dao.DocumentLanguageDAO;
import reference.model.DocumentLanguage;

@Command(name = "fill_doc_langs")
public class FillDocumentLanguages extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<DocumentLanguage> entities = new ArrayList<>();

		LanguageCode langs[] = { LanguageCode.RUS, LanguageCode.ENG, LanguageCode.KAZ, LanguageCode.CHI,
				LanguageCode.BUL, LanguageCode.DEU, LanguageCode.BEL, LanguageCode.POR, LanguageCode.SPA };
		for (LanguageCode code : langs) {
			Language lang = ServerConst.getLanguage(code);
			DocumentLanguage docLang = new DocumentLanguage();
			docLang.setCode(code);
			docLang.setName(lang.getName());
			docLang.setLocName(lang.getLocName());
			entities.add(docLang);
		}

		try {
			DocumentLanguageDAO dao = new DocumentLanguageDAO(ses);
			for (DocumentLanguage entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.infoLogEntry(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				} catch (SecureException e) {
					logger.errorLogEntry(e);
				}
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
		}
		logger.infoLogEntry("done...");
	}

}
