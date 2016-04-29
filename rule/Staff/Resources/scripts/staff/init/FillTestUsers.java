package staff.init;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.env.EnvConst;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.util.Util;

import administrator.dao.UserDAO;
import administrator.model.User;
import reference.dao.PositionDAO;
import reference.model.Position;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Organization;
import staff.model.Role;

/**
 * 
 * 
 * @author Kayra created 24-01-2016
 */

public class FillTestUsers extends InitialDataAdapter<Employee, EmployeeDAO> {
	private _Session ses;
	private static String file1 = EnvConst.RESOURCES_DIR + File.separator + "Roman.txt";
	private static String file2 = EnvConst.RESOURCES_DIR + File.separator + "Fantasy.txt";

	@Override
	public List<Employee> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {
		this.ses = ses;
		List<Employee> entities = new ArrayList<Employee>();
		if (checkNecessaryFiles()) {
			UserDAO uDao = new UserDAO();
			List<User> users = uDao.findAll();
			int rCount = users.size();
			System.out.println("System users count = " + rCount);
			for (User u : users) {
				entities.add(getMock(u));
			}
		} else {
			System.out.println("There is no \"" + file1 + "\" or \"" + file2 + "\" file");
		}
		return entities;
	}

	private Employee getMock(User user) {
		Employee emp = new Employee();
		emp.setUser(user);
		emp.setName(getRndFirstName() + " " + getRndLastName());
		Organization o = new OrganizationDAO(ses).findAll().get(1);
		emp.setOrganization(o);
		RoleDAO roleDao = new RoleDAO(ses);
		List<Role> rl = roleDao.findAll();
		Role role = (Role) Util.getRndListElement(rl);
		if (role != null) {
			emp.addRole(role);
		}

		PositionDAO postDao = new PositionDAO(ses);
		List<Position> posts = postDao.findAll();
		emp.setPosition((Position) Util.getRndListElement(posts));
		return emp;

	}

	private String getRndFirstName() {
		try {
			NameGenerator n = new NameGenerator(file1);
			return n.compose(3);
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
		}
		return "";
	}

	private String getRndLastName() {
		try {
			NameGenerator n = new NameGenerator(file2);
			return n.compose(3);
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
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
