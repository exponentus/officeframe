package staff.task;

import administrator.dao.UserDAO;
import administrator.model.User;
import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.legacy.smartdoc.ImportNSF;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.server.Server;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;
import com.exponentus.util.NumberUtil;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import reference.dao.PositionDAO;
import reference.model.Position;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Organization;
import staff.model.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Command(name = "import_rvz_nsf")
public class ImportRvzFromNSF extends ImportNSF {
	private static final String RVZ_ROLE = "senior_manager";

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Map<String, Employee> entities = new HashMap<>();
		try {
			UserDAO uDao = new UserDAO(ses);
			OrganizationDAO oDao = new OrganizationDAO(ses);
			EmployeeDAO eDao = new EmployeeDAO(ses);
			PositionDAO pDao = new PositionDAO(ses);
			RoleDAO rDao = new RoleDAO(ses);
			List<Organization> orgs = oDao.findPrimaryOrg();
			if (orgs != null && orgs.size() > 0) {
				Organization primaryOrg = orgs.get(0);
				List<Role> roles = new ArrayList<>();
				Role role = rDao.findByName(RVZ_ROLE);
				if (role != null) {
					roles.add(role);
					try {
						ViewEntryCollection vec = getAllEntries("struct.nsf");
						ViewEntry entry = vec.getFirstEntry();
						ViewEntry tmpEntry = null;
						while (entry != null) {
							Document doc = entry.getDocument();
							String form = doc.getItemValueString("Form");
							if (form.equals("E") && doc.getItemValueString("EType").equals("RVZ")) {
								String unId = doc.getUniversalID();
								Employee entity = eDao.findByExtKey(unId);
								if (entity == null) {
									entity = new Employee();
									entity.setAuthor(new SuperUser());
								}
								String na = doc.getItemValueString("NotesAddress");
								IUser user = uDao.findByExtKey(doc.getItemValueString("NotesAddress"));
								if (user != null) {
									try {
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
										entity.setRoles(roles);
										entity.setUser((User) user);
										entities.put(doc.getUniversalID(), entity);
									} catch (DAOException e) {
										logger.exception(e);
									}
								} else {
									logger.error("\"" + na + "\" user has not been found");
								}
							}
							tmpEntry = vec.getNextEntry();
							entry.recycle();
							entry = tmpEntry;
						}
					} catch (NotesException e) {
						logger.exception(e);
					}
					
					logger.info("has been found " + entities.size() + " records");
					
					for (Entry<String, Employee> entry : entities.entrySet()) {
						save(eDao, entry.getValue(), entry.getKey());
					}
				} else {
					logger.error("RVZ role has not been found");
				}
			} else {
				logger.error("primary Organization has not been found");
			}
		} catch (DAOException e) {
			Server.logger.exception(e);
		}
		logger.info("done...");
	}

}
