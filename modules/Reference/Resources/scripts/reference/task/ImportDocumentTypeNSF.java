package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.DocumentTypeDAO;
import reference.model.DocumentType;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Command(name = "import_vid_nsf")
public class ImportDocumentTypeNSF extends ImportNSF {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        Map<String, DocumentType> entities = new HashMap<>();
        try {
            DocumentTypeDAO dao = new DocumentTypeDAO(ses);

            try {
                ViewEntryCollection vec = getAllEntries("sprav.nsf");
                ViewEntry entry = vec.getFirstEntry();
                ViewEntry tmpEntry = null;
                while (entry != null) {
                    Document doc = entry.getDocument();
                    String form = doc.getItemValueString("Form");
                    if (form.equals("TypeDoc")) {
                        String unId = doc.getUniversalID();
                        DocumentType entity = dao.findByExtKey(unId);
                        if (entity == null) {
                            entity = new DocumentType();
                            entity.setAuthor(new SuperUser());
                        }
                        entity.setName(doc.getItemValueString("Name"));
                        Map<LanguageCode, String> localizedNames = new HashMap<>();
                        localizedNames.put(LanguageCode.RUS, doc.getItemValueString("Name"));
                        localizedNames.put(LanguageCode.KAZ, doc.getItemValueString("Name1"));
                        entity.setLocName(localizedNames);
                        entity.setCategory(doc.getItemValueString("Cat"));
                        entity.setPrefix("");
                        entities.put(doc.getUniversalID(), entity);
                    }
                    tmpEntry = vec.getNextEntry();
                    entry.recycle();
                    entry = tmpEntry;
                }
            } catch (NotesException e) {
                logger.exception(e);
            }

            logger.info("has been found " + entities.size() + " records");

            for (Entry<String, DocumentType> entry : entities.entrySet()) {
                save(dao, entry.getValue(), entry.getKey());
            }
        } catch (DAOException e) {
            logger.exception(e);
        }
        logger.info("done...");
    }

}