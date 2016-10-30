package reference.tasks;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPatch;
import com.exponentus.scriptprocessor.tasks.Command;

import administrator.init.ServerConst;
import administrator.model.Language;
import reference.dao.DocumentLanguageDAO;
import reference.model.DocumentLanguage;

@Command(name = "fill_doc_langs")
public class FillDocumentLanguages extends _DoPatch {

	@Override
	public void doTask(_Session ses) {
		List<DocumentLanguage> entities = new ArrayList<>();
		DocumentLanguageDAO dao = new DocumentLanguageDAO(ses);
		LanguageCode langs[] = { LanguageCode.RUS, LanguageCode.ENG, LanguageCode.KAZ, LanguageCode.FRA, LanguageCode.CHI, LanguageCode.DEU,
		        LanguageCode.POL, LanguageCode.BEL, LanguageCode.CES, LanguageCode.GRE, LanguageCode.UKR, LanguageCode.TUR, LanguageCode.ITA,
		        LanguageCode.KOR, LanguageCode.JPN, LanguageCode.SPA, LanguageCode.HIN, LanguageCode.ARA };
		for (LanguageCode code : langs) {
			Language lang = ServerConst.getLanguage(code);
			DocumentLanguage docLang = new DocumentLanguage();
			docLang.setCode(code);
			docLang.setName(lang.getName());
			docLang.setLocalizedName(lang.getLocalizedName());
			entities.add(docLang);
		}

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
		logger.infoLogEntry("done...");
	}

}
