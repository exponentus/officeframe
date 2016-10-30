package staff.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.legacy.ConvertorEnvConst;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;
import com.exponentus.util.NumberUtil;

import administrator.dao.UserDAO;
import administrator.model.User;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.PositionDAO;
import reference.model.Position;
import staff.dao.DepartmentDAO;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.model.Department;
import staff.model.Employee;
import staff.model.Organization;

@Command(name = "import_emp_nsf")
public class ImportEmpFromNSF extends ImportNSF {

	@Override
	public void doTask(_Session ses) {
		Map<String, Employee> entities = new HashMap<>();
		UserDAO uDao = new UserDAO(ses);
		OrganizationDAO oDao = new OrganizationDAO(ses);
		DepartmentDAO dDao = new DepartmentDAO(ses);
		EmployeeDAO eDao = new EmployeeDAO(ses);
		PositionDAO pDao = new PositionDAO(ses);
		Organization primaryOrg = oDao.findPrimaryOrg();

		User dummyUser = (User) uDao.findByLogin(ConvertorEnvConst.DUMMY_USER);

		if (primaryOrg != null) {
			try {
				ViewEntryCollection vec = getAllEntries("struct.nsf");
				ViewEntry entry = vec.getFirstEntry();
				ViewEntry tmpEntry = null;
				while (entry != null) {
					Document doc = entry.getDocument();
					String form = doc.getItemValueString("Form");
					if (form.equals("E") && doc.getItemValueString("EType").equals("EMP")) {
						try {
							String unId = doc.getUniversalID();
							Employee entity = eDao.findByExtKey(unId);
							if (entity == null) {
								entity = new Employee();
								entity.setAuthor(new SuperUser());
							}
							String na = doc.getItemValueString("NotesAddress");
							String parent = doc.getParentDocumentUNID();
							Employee parentUser = eDao.findByExtKey(parent);
							if (parentUser != null) {
								entity.setBoss(parentUser);
							} else {
								Department parentDep = dDao.findByExtKey(parent);
								if (parentDep != null) {
									entity.setDepartment(parentDep);
								} else {
									logger.errorLogEntry("\"" + parent + "\" parent entity has not been found");
									break;
								}
							}
							entity.setName(doc.getItemValueString("FullName"));
							entity.setRank(NumberUtil.stringToInt(doc.getItemValueString("Rank"), 998));
							String stuff = doc.getItemValueString("Stuff");
							Position position = pDao.findByName(stuff);
							if (position != null) {
								entity.setPosition(position);
							}
							IUser<Long> user = uDao.findByExtKey(doc.getItemValueString("NotesAddress"));
							if (user != null) {
								entity.setUser((User) user);
							} else {
								logger.errorLogEntry("\"" + na + "\" user has not been found");
								entity.setUser(dummyUser);
							}
							entities.put(doc.getUniversalID(), entity);
						} catch (DAOException e) {
							logger.errorLogEntry(e);
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
				save(eDao, entry.getValue(), entry.getKey());
			}

		} else {
			logger.errorLogEntry("primary Organization has not been found");
		}
		System.out.println("done...");
	}

}
