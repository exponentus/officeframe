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
import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;
import staff.dao.DepartmentDAO;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.model.Department;
import staff.model.Employee;
import staff.model.Organization;

@Command(name = "import_dep_nsf")
public class ImportDepFromNSF extends _DoPatch {

	@Override
	public void doTask(_Session ses) {
		Map<String, Department> entities = new HashMap<>();
		OrganizationDAO oDao = new OrganizationDAO(ses);
		DepartmentDAO dDao = new DepartmentDAO(ses);
		EmployeeDAO eDao = new EmployeeDAO(ses);
		DepartmentTypeDAO dtDao = new DepartmentTypeDAO(ses);
		CollationDAO cDao = new CollationDAO(ses);
		Map<String, String> depTypeCollation = depTypeCollationMapInit();
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
					if (form.equals("D")) {
						Department entity = new Department();
						String parent = doc.getParentDocumentUNID();
						Employee parentUser = eDao.findByExtKey(parent);
						if (parentUser != null) {
							entity.setBoss(parentUser);
						} else {
							Department parentDep = dDao.findByExtKey(parent);
							if (parentDep != null) {
								entity.setLeadDepartment(parentDep);
							} else {
								entity.setOrganization(primaryOrg);
							}
						}
						entity.setName(doc.getItemValueString("FullName"));
						String depType = doc.getItemValueString("Type");
						String intRefKey = depTypeCollation.get(depType);
						if (intRefKey == null) {
							logger.errorLogEntry("wrong reference ext value \"" + depType + "\"");
							intRefKey = "undefined";
						}
						DepartmentType type = dtDao.findByName(intRefKey);
						entity.setType(type);
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

			for (Entry<String, Department> entry : entities.entrySet()) {
				Department dep = entry.getValue();

				try {
					if (dDao.add(dep) != null) {
						Collation collation = new Collation();
						collation.setExtKey(entry.getKey());
						collation.setIntKey(dep.getId());
						collation.setEntityType(Department.class.getName());
						cDao.add(collation);
						logger.infoLogEntry(dep.getName() + " added");
					}

				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				} catch (SecureException e) {
					logger.errorLogEntry(e);
				}

			}
		}

		logger.infoLogEntry("done...");

	}

	private Map<String, String> depTypeCollationMapInit() {
		Map<String, String> depTypeCollation = new HashMap<>();
		depTypeCollation.put("Департамент", "Department");
		depTypeCollation.put("Отдел", "Department");
		depTypeCollation.put("Управление", "Management");

		depTypeCollation.put("Сектор", "Sector");
		depTypeCollation.put("Группа", "Group");

		depTypeCollation.put("null", "undefined");
		return depTypeCollation;

	}

}
