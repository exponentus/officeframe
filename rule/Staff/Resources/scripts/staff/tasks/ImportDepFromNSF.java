package staff.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.legacy.ConvertorEnvConst;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;

import lotus.domino.Document;
import lotus.domino.NotesException;
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
public class ImportDepFromNSF extends ImportNSF {
	
	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Map<String, Department> entities = new HashMap<>();
		try {
			OrganizationDAO oDao = new OrganizationDAO(ses);
			DepartmentDAO dDao = new DepartmentDAO(ses);
			EmployeeDAO eDao = new EmployeeDAO(ses);
			DepartmentTypeDAO dtDao = new DepartmentTypeDAO(ses);
			Map<String, String> depTypeCollation = depTypeCollationMapInit();
			List<Organization> orgs = oDao.findPrimaryOrg();
			if (orgs != null && orgs.size() > 0) {
				Organization primaryOrg = orgs.get(0);
				try {
					ViewEntryCollection vec = getAllEntries("struct.nsf");
					ViewEntry entry = vec.getFirstEntry();
					ViewEntry tmpEntry = null;
					while (entry != null) {
						Document doc = entry.getDocument();
						String form = doc.getItemValueString("Form");
						if (form.equals("D")) {
							try {
								String unId = doc.getUniversalID();
								Department entity = dDao.findByExtKey(unId);
								if (entity == null) {
									entity = new Department();
									entity.setAuthor(new SuperUser());
								}
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
									intRefKey = ConvertorEnvConst.GAG_KEY;
								}
								DepartmentType type;
								type = dtDao.findByName(intRefKey);
								entity.setType(type);
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
				
				for (Entry<String, Department> entry : entities.entrySet()) {
					save(eDao, entry.getValue(), entry.getKey());
				}
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
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
		
		depTypeCollation.put("", ConvertorEnvConst.GAG_KEY);
		depTypeCollation.put("null", ConvertorEnvConst.GAG_KEY);
		return depTypeCollation;
		
	}
	
}
