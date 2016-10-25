package staff.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPatch;
import com.exponentus.scriptprocessor.tasks.Command;

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
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;
import staff.dao.OrganizationDAO;
import staff.model.Organization;

@Command(name = "import_orgs_nsf")
public class LoadOrgsFromNSF extends _DoPatch {
	private static final String DOMINO_HOST = "localhost";
	private static final String DOMINO_USER = "developer";
	private static final String DOMINO_USER_PWD = "12345";
	private static final String STRUCTURE_DATABASE = "SmartDoc_BRK\\struct.nsf";

	@Override
	public void doTask(_Session ses) {
		Map<String, Organization> entities = new HashMap<>();
		OrgCategoryDAO ocDao = new OrgCategoryDAO(ses);
		CollationDAO<Long> cDao = new CollationDAO(ses);

		try {
			Session dominoSession = NotesFactory.createSession(DOMINO_HOST, DOMINO_USER, DOMINO_USER_PWD);
			Database inDb = dominoSession.getDatabase(dominoSession.getServerName(), STRUCTURE_DATABASE);
			View view = inDb.getView("(AllUNID)");
			ViewEntryCollection vec = view.getAllEntries();
			ViewEntry entry = vec.getFirstEntry();
			ViewEntry tmpEntry = null;
			while (entry != null) {
				Document doc = entry.getDocument();
				String form = doc.getItemValueString("Form");
				if (form.equals("Corr")) {
					Organization entity = new Organization();
					entity.setName(doc.getItemValueString("FullName"));
					Map<LanguageCode, String> localizedNames = new HashMap<>();
					localizedNames.put(LanguageCode.RUS, doc.getItemValueString("FullName"));
					entity.setLocalizedName(localizedNames);
					String typeCorr = doc.getItemValueString("TypeCorr");
					if (typeCorr != null) {
						OrgCategory oCat = ocDao.findByName(typeCorr);
						if (oCat != null) {
							entity.setOrgCategory(oCat);
						}
					}

					entities.put(doc.getUniversalID(), entity);
				}
				tmpEntry = vec.getNextEntry();
				entry.recycle();
				entry = tmpEntry;
			}
		} catch (NotesException e) {
			logger.errorLogEntry(e);
		}

		System.out.println("has been found " + entities.size() + " records");
		OrganizationDAO oDao = new OrganizationDAO(ses);
		for (Entry<String, Organization> entry : entities.entrySet()) {
			Organization org = entry.getValue();
			try {
				try {
					if (oDao.add(org) != null) {
						Collation<Long> collation = new Collation<>();
						collation.setExtKey(entry.getKey());
						collation.setIntKey(org.getId().toString());
						collation.setEntityType(org.getClass().getName());
						cDao.add(collation);
					}
					System.out.println(org.getName() + " added");
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.VALUE_IS_NOT_UNIQUE) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				}

			} catch (SecureException e) {
				logger.errorLogEntry(e);
			}
		}
		System.out.println("done...");
	}

}
