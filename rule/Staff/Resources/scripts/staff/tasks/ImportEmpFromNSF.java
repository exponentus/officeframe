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

import administrator.dao.CollationDAO;
import administrator.dao.UserDAO;
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
import staff.dao.DepartmentDAO;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.model.Department;
import staff.model.Employee;
import staff.model.Organization;

@Command(name = "import_emp_nsf")
public class ImportEmpFromNSF extends _DoPatch {

	@Override
	public void doTask(_Session ses) {
		Map<String, Employee> entities = new HashMap<>();
		UserDAO uDao = new UserDAO(ses);
		OrganizationDAO oDao = new OrganizationDAO(ses);
		DepartmentDAO dDao = new DepartmentDAO(ses);
		EmployeeDAO eDao = new EmployeeDAO(ses);
		PositionDAO pDao = new PositionDAO(ses);
		CollationDAO cDao = new CollationDAO(ses);
		Map<String, String> stuffCollation = stuffCollationMapInit();
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
					if (form.equals("E") && doc.getItemValueString("EType").equals("EMP")) {
						Employee entity = new Employee();
						String na = doc.getItemValueString("NotesAddress");
						IUser<Long> user = uDao.findByExtKey(doc.getItemValueString("NotesAddress"));
						if (user != null) {
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
							entity.setRank(doc.getItemValueInteger("Rank"));
							String stuff = doc.getItemValueString("Stuff");
							Position position = pDao.findByName(stuff);
							if (position != null) {
								entity.setPosition(position);
							}

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
		System.out.println("done...");
	}

	private Map<String, String> stuffCollationMapInit() {
		Map<String, String> typeCorrCollation = new HashMap<>();
		typeCorrCollation.put("ТОО", "LTD");
		typeCorrCollation.put("Банки", "Bank");
		typeCorrCollation.put("Компании", "LTD");
		typeCorrCollation.put("Компания", "LTD");
		typeCorrCollation.put("ООО", "LTD");
		typeCorrCollation.put("Фирма", "LTD");
		typeCorrCollation.put("Gmbh", "LTD");
		typeCorrCollation.put("Кооператив", "LTD");
		typeCorrCollation.put("Кооператив", "LTD");
		typeCorrCollation.put("АО", "JSC");
		typeCorrCollation.put("ГУ", "State_office");
		typeCorrCollation.put("ИНТЕГРАЦИЯ", "Integration");
		typeCorrCollation.put("ОАО", "JSC");
		typeCorrCollation.put("Министерства РК", "Ministry");
		typeCorrCollation.put("Премьер-Министр РК", "Ministry");
		typeCorrCollation.put("", "undefined");
		typeCorrCollation.put("Суд", "Court");
		typeCorrCollation.put("Фонды", "Court");
		typeCorrCollation.put("РГП", "State_enterprise");
		typeCorrCollation.put("ГКП", "State_enterprise");
		typeCorrCollation.put("РГКП", "State_enterprise");
		typeCorrCollation.put("Комитеты", "Committee");
		typeCorrCollation.put("Зарубежная компания", "International_company");
		typeCorrCollation.put("Прокуратура", "State_enterprise");
		typeCorrCollation.put("Агентства РК", "State_enterprise");
		typeCorrCollation.put("Агентства", "State_enterprise");
		typeCorrCollation.put("Агентство", "State_enterprise");
		typeCorrCollation.put("Управление", "State_enterprise");
		typeCorrCollation.put("Управления", "State_enterprise");
		typeCorrCollation.put("Министрества РК", "State_enterprise");
		typeCorrCollation.put("Правительство", "State_enterprise");
		typeCorrCollation.put("Акиматы", "City_Hall");
		typeCorrCollation.put("ЗАО", "JSC");
		typeCorrCollation.put("ИП", "Self_employed");
		typeCorrCollation.put("Предприниматель", "Self_employed");
		typeCorrCollation.put("Союзы", "Public_association");
		typeCorrCollation.put("Филиал", "Branch");
		typeCorrCollation.put("Посольство", "Embassy");
		typeCorrCollation.put("Посольство в РК", "Embassy");
		typeCorrCollation.put("Посольства РК за рубежом", "Embassy");
		typeCorrCollation.put("Университет", "Educational_institution");

		typeCorrCollation.put("null", "undefined");
		return typeCorrCollation;

	}

}
