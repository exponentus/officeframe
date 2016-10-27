package staff.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.legacy.domino.DominoEnvConst;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPatch;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;
import com.exponentus.util.NumberUtil;

import administrator.dao.CollationDAO;
import administrator.dao.UserDAO;
import administrator.model.Collation;
import administrator.model.User;
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
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.model.Employee;
import staff.model.Organization;

@Command(name = "import_rvz_nsf")
public class ImportRvzFromNSF extends _DoPatch {

	@Override
	public void doTask(_Session ses) {
		Map<String, Employee> entities = new HashMap<>();
		UserDAO uDao = new UserDAO(ses);
		OrganizationDAO oDao = new OrganizationDAO(ses);
		EmployeeDAO eDao = new EmployeeDAO(ses);
		PositionDAO pDao = new PositionDAO(ses);
		CollationDAO cDao = new CollationDAO(ses);
		Organization primaryOrg = oDao.findPrimaryOrg();
		if (primaryOrg != null) {
			try {
				Session dominoSession = NotesFactory.createSession(DominoEnvConst.DOMINO_HOST, DominoEnvConst.DOMINO_USER,
				        DominoEnvConst.DOMINO_USER_PWD);
				Database inDb = dominoSession.getDatabase(dominoSession.getServerName(), DominoEnvConst.APPLICATION_DIRECTORY + "struct.nsf");
				View view = inDb.getView("(AllUNID)");
				ViewEntryCollection vec = view.getAllEntries();
				ViewEntry entry = vec.getFirstEntry();
				ViewEntry tmpEntry = null;
				while (entry != null) {
					Document doc = entry.getDocument();
					String form = doc.getItemValueString("Form");
					if (form.equals("E") && doc.getItemValueString("EType").equals("RVZ")) {
						Employee entity = new Employee();
						entity.setAuthor(new SuperUser());
						String na = doc.getItemValueString("NotesAddress");
						IUser<Long> user = uDao.findByExtKey(doc.getItemValueString("NotesAddress"));
						if (user != null) {
							String parent = doc.getParentDocumentUNID();
							Employee parentUser = eDao.findByExtKey(parent);
							if (parentUser == null) {
								entity.setOrganization(primaryOrg);
							} else {
								entity.setBoss(parentUser);
							}
							entity.setName(doc.getItemValueString("FullName"));
							entity.setRank(NumberUtil.stringToInt(doc.getItemValueString("Rank"), 998));
							String stuff = doc.getItemValueString("Stuff");
							Position position = pDao.findByName(stuff);
							if (position != null) {
								entity.setPosition(position);
							}
							entity.setUser((User) user);
							entities.put(doc.getUniversalID(), entity);
						} else {
							logger.errorLogEntry("\"" + na + "\" user has not been found");
						}
					}
					tmpEntry = vec.getNextEntry();
					entry.recycle();
					entry = tmpEntry;
				}
			} catch (NotesException e) {
				logger.errorLogEntry(e);
			}

			logger.infoLogEntry("has been found " + entities.size() + " records");

			for (Entry<String, Employee> entry : entities.entrySet()) {
				Employee employee = entry.getValue();

				try {
					if (eDao.add(employee) != null) {
						Collation collation = new Collation();
						collation.setExtKey(entry.getKey());
						collation.setIntKey(employee.getId());
						collation.setEntityType(employee.getClass().getName());
						cDao.add(collation);
						logger.infoLogEntry(employee.getName() + " added");
					}

				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a data is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				} catch (SecureException e) {
					logger.errorLogEntry(e);
				}

			}
		} else {
			logger.errorLogEntry("primary Organization has not been found");
		}
		logger.infoLogEntry("done...");
	}

}
