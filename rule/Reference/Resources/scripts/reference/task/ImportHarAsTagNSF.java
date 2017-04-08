package reference.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;
import com.exponentus.util.StringUtil;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.TagDAO;
import reference.model.Tag;

@Command(name = "import_har_as_tag_nsf")
public class ImportHarAsTagNSF extends ImportNSF {
	private static final String sdCatName = "ЦОД";
	private static final String tagCatName = "incoming";
	
	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Map<String, Tag> entities = new HashMap<>();
		try {
			TagDAO dao = new TagDAO(ses);
			
			try {
				ViewEntryCollection vec = getAllEntries("sprav.nsf");
				ViewEntry entry = vec.getFirstEntry();
				ViewEntry tmpEntry = null;
				while (entry != null) {
					Document doc = entry.getDocument();
					String form = doc.getItemValueString("Form");
					String sdCat = doc.getItemValueString("Cat");
					if (form.equals("Har") && sdCat.equals(sdCatName)) {
						Tag entity = dao.findByExtKey(doc.getUniversalID());
						if (entity == null) {
							entity = new Tag();
							entity.setAuthor(new SuperUser());
						}
						entity.setName(doc.getItemValueString("Name"));
						Map<LanguageCode, String> localizedNames = new HashMap<>();
						localizedNames.put(LanguageCode.RUS, doc.getItemValueString("Name1"));
						localizedNames.put(LanguageCode.KAZ, doc.getItemValueString("Name2"));
						entity.setLocName(localizedNames);
						entity.setCategory(tagCatName);
						entity.setColor(StringUtil.getRandomColor());
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
			
			for (Entry<String, Tag> entry : entities.entrySet()) {
				save(dao, entry.getValue(), entry.getKey());
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
		}
		logger.infoLogEntry("done...");
	}

}
