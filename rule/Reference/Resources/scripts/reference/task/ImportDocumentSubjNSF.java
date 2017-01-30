package reference.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.DocumentSubjectDAO;
import reference.model.DocumentSubject;

@Command(name = "import_har_nsf")
public class ImportDocumentSubjNSF extends ImportNSF {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Map<String, DocumentSubject> entities = new HashMap<>();
		try {
			DocumentSubjectDAO dao = new DocumentSubjectDAO(ses);
			try {
				ViewEntryCollection vec = getAllEntries("sprav.nsf");
				ViewEntry entry = vec.getFirstEntry();
				ViewEntry tmpEntry = null;
				while (entry != null) {
					Document doc = entry.getDocument();
					String form = doc.getItemValueString("Form");
					if (form.equals("Har")) {
						String unId = doc.getUniversalID();
						DocumentSubject entity = dao.findByExtKey(unId);
						if (entity == null) {
							entity = new DocumentSubject();
							entity.setAuthor(new SuperUser());
						}
						entity.setName(doc.getItemValueString("Name"));
						Map<LanguageCode, String> localizedNames = new HashMap<>();
						localizedNames.put(LanguageCode.RUS, doc.getItemValueString("Name"));
						localizedNames.put(LanguageCode.KAZ, doc.getItemValueString("NameKZ"));
						entity.setLocName(localizedNames);
						entity.setCategory(doc.getItemValueString("Cat"));
						entities.put(doc.getUniversalID(), entity);
					}
					tmpEntry = vec.getNextEntry();
					entry.recycle();
					entry = tmpEntry;
				}
			} catch (NotesException e) {
				logger.errorLogEntry(e);
			}

			logger.infoLogEntry("has been found " + entities.size() + " records");

			for (Entry<String, DocumentSubject> entry : entities.entrySet()) {
				save(dao, entry.getValue(), entry.getKey());
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
		}
		logger.infoLogEntry("done...");
	}

}
