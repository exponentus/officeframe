package reference.task;

import administrator.init.ServerConst;
import administrator.model.Language;
import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.DocumentLanguageDAO;
import reference.model.DocumentLanguage;

import java.util.ArrayList;
import java.util.List;

@Command(name = "fill_doc_langs")
public class FillDocumentLanguages extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<DocumentLanguage> entities = new ArrayList<>();

        LanguageCode langs[] = {LanguageCode.RUS, LanguageCode.ENG, LanguageCode.KAZ, LanguageCode.POR, LanguageCode.SPA};
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
