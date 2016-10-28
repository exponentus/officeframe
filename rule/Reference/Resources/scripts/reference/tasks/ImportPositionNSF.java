package reference.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.legacy.ConvertorEnvConst;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPatch;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.PositionDAO;
import reference.model.Position;

@Command(name = "import_positions_nsf")
public class ImportPositionNSF extends _DoPatch {

	@Override
	public void doTask(_Session ses) {
		Map<String, Position> entities = new HashMap<>();
		CollationDAO cDao = new CollationDAO(ses);

		try {
			Session dominoSession = NotesFactory.createSession(ConvertorEnvConst.DOMINO_HOST, ConvertorEnvConst.DOMINO_USER,
			        ConvertorEnvConst.DOMINO_USER_PWD);
			Database inDb = dominoSession.getDatabase(dominoSession.getServerName(), ConvertorEnvConst.APPLICATION_DIRECTORY + "sprav.nsf");
			View view = inDb.getView("(AllUNID)");
			ViewEntryCollection vec = view.getAllEntries();
			ViewEntry entry = vec.getFirstEntry();
			ViewEntry tmpEntry = null;
			while (entry != null) {
				Document doc = entry.getDocument();
				String form = doc.getItemValueString("Form");
				if (form.equals("Post")) {
					Position entity = new Position();
					entity.setAuthor(new SuperUser());
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
		PositionDAO oDao = new PositionDAO(ses);
		for (Entry<String, Position> entry : entities.entrySet()) {
			Position entity = entry.getValue();
			try {
				try {
					if (oDao.add(entity) != null) {
						Collation collation = new Collation();
						collation.setExtKey(entry.getKey());
						collation.setIntKey(entity.getId());
						collation.setEntityType(entity.getClass().getName());
						cDao.add(collation);
						logger.infoLogEntry(entity.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				}

			} catch (SecureException e) {
				logger.errorLogEntry(e);
			}
		}
		logger.infoLogEntry("done...");
	}

}
