package reference.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.appenv.AppEnv;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.PositionDAO;
import reference.model.Position;

@Command(name = "import_positions_nsf")
public class ImportPositionNSF extends ImportNSF {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Map<String, Position> entities = new HashMap<>();
		PositionDAO dao = new PositionDAO(ses);

		try {
			ViewEntryCollection vec = getAllEntries("sprav.nsf");
			ViewEntry entry = vec.getFirstEntry();
			ViewEntry tmpEntry = null;
			while (entry != null) {
				Document doc = entry.getDocument();
				String form = doc.getItemValueString("Form");
				if (form.equals("Post")) {
					Position entity = dao.findByExtKey(doc.getUniversalID());
					if (entity == null) {
						entity = new Position();
						entity.setAuthor(new SuperUser());
					}
					entity.setName(doc.getItemValueString("Name"));
					Map<LanguageCode, String> localizedNames = new HashMap<>();
					localizedNames.put(LanguageCode.RUS, doc.getItemValueString("Name1"));
					localizedNames.put(LanguageCode.KAZ, doc.getItemValueString("Name2"));
					entity.setLocalizedName(localizedNames);
					entity.setRank(doc.getItemValueInteger("Rank"));
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

		for (Entry<String, Position> entry : entities.entrySet()) {
			save(dao, entry.getValue(), entry.getKey());
		}

		logger.infoLogEntry("done...");
	}

}
