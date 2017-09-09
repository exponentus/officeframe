package staff.task;


import administrator.dao.UserDAO;
import administrator.model.User;
import com.exponentus.appenv.AppEnv;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.server.Server;
import com.exponentus.util.EnumUtil;
import reference.dao.PositionDAO;
import reference.model.Position;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Organization;
import staff.model.Role;
import staff.task.helper.NameGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Command(name = "gen_test_emps")
public class GenerateTestEmployees extends Do {
	private static String file1 = EnvConst.RESOURCES_DIR + File.separator + "Roman.txt";
	private static String file2 = EnvConst.RESOURCES_DIR + File.separator + "Simple.txt";

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Employee> entities = new ArrayList<Employee>();
		if (checkNecessaryFiles()) {
			try {
				ViewPage<Organization> orgs = new OrganizationDAO(ses).findAll();
				if (orgs.getCount() > 0) {
					Organization org = orgs.getResult().get(1);
					UserDAO uDao = new UserDAO();
					List<User> users = uDao.findAll();
					int rCount = users.size();
					System.out.println("System users count = " + rCount);
					for (User u : users) {
						entities.add(getMock(u, ses, org));
					}

					EmployeeDAO oDao = new EmployeeDAO(ses);
					for (Employee entry : entities) {
						save(oDao, entry);
					}

				} else {
					System.out.println("There is no any organization");
				}
			} catch (DAOException e) {
				logger.exception(e);
			}

		} else {
			System.out.println("There is no \"" + file1 + "\" or \"" + file2 + "\" file");
		}

		logger.info("done...");

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void save(IDAO dao, IAppEntity entity) {

		try {
			if (dao.add(entity) != null) {
				logger.info(entity.getTitle() + " added");
			}

		} catch (DAOException e) {
			if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
				logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
			} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
				logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
			} else {
				logger.exception(e);
			}
		} catch (SecureException e) {
			logger.exception(e);
		}
	}

	private Employee getMock(User user, _Session ses, Organization o) {
		Employee emp = new Employee();
		emp.setUser(user);
		emp.setName(getRndFirstName() + " " + getRndLastName());
		try {
			emp.setOrganization(o);
			RoleDAO roleDao = new RoleDAO(ses);
			List<Role> rl = roleDao.findAll().getResult();
			Role role = EnumUtil.getRndElement(rl);
			if (role != null) {
				emp.addRole(role);
			}

			PositionDAO postDao = new PositionDAO(ses);
			List<Position> posts = postDao.findAll().getResult();
			emp.setPosition(EnumUtil.getRndElement(posts));

		} catch (DAOException e) {
			Server.logger.exception(e);
		}
		emp.setTitle(emp.getName());
		return emp;

	}

	private String getRndFirstName() {
		try {
			staff.task.helper.NameGenerator n = new staff.task.helper.NameGenerator(file1);
			return n.compose(3);
		} catch (IOException e) {
			Server.logger.exception(e);
		}
		return "";
	}

	private String getRndLastName() {
		try {
			NameGenerator n = new NameGenerator(file2);
			return n.compose(3);
		} catch (IOException e) {
			Server.logger.exception(e);
		}
		return "";
	}

	private boolean checkNecessaryFiles() {
		File f1 = new File(file1);
		File f2 = new File(file2);
		if (f1.exists() && f2.exists()) {
			return true;
		}
		return false;

	}

}
