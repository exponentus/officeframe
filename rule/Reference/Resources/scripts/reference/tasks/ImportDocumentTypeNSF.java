package reference.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;

import administrator.dao.CollationDAO;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.DocumentTypeDAO;
import reference.model.DocumentType;

@Command(name = "import_vid_nsf")
public class ImportDocumentTypeNSF extends ImportNSF {

	@Override
	public void doTask(_Session ses) {
		Map<String, DocumentType> entities = new HashMap<>();
		CollationDAO cDao = new CollationDAO(ses);
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
					entity.setLocalizedName(localizedNames);
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

		for (Entry<String, DocumentType> entry : entities.entrySet()) {
			save(dao, entry.getValue(), entry.getKey());
		}

		logger.infoLogEntry("done...");
	}

}
