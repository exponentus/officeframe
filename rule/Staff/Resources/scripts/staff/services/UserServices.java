package staff.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DatabasePoolException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.dataengine.jpa.constants.AppCode;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.user.AnonymousUser;

import administrator.dao.ApplicationDAO;
import administrator.dao.UserDAO;
import administrator.model.Application;
import administrator.model.User;
import kz.flabs.dataengine.ISystemDatabase;
import reference.dao.PositionDAO;
import reference.model.Position;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.dao.OrganizationLabelDAO;
import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Organization;
import staff.model.OrganizationLabel;
import staff.model.Role;

public class UserServices {
	private AppEnv env = Environment.getAppEnv(EnvConst.ADMINISTRATOR_APP_NAME);

	@Deprecated
	public void importFromH2() {
		importFromH2(true);
	}

	@SuppressWarnings("deprecation")
	public void importFromH2(boolean showConsoleOutput) {
		List<User> entities = new ArrayList<User>();

		ISystemDatabase sysDb = null;
		try {
			sysDb = new kz.flabs.dataengine.h2.SystemDatabase();

			List<kz.flabs.users.User> users = sysDb.getAllUsers("", 0, 10000);
			int rCount = users.size();
			if (showConsoleOutput) {
				System.out.println("System users count = " + rCount);
			}

			ApplicationDAO aDao = new ApplicationDAO(new _Session(env, new AnonymousUser()));
			List<AppCode> list = new ArrayList<AppCode>();
			list.add(AppCode.CUSTOM);
			List<Application> appList = aDao.findAllin("code", list, 0, 0).getResult();

			for (kz.flabs.users.User oldUser : users) {
				User entity = new User();
				entity.setLogin(oldUser.getLogin());
				entity.setPwd(oldUser.getPassword());
				entity.setPwdHash(oldUser.getPasswordHash());
				entity.setAllowedApps(appList);

				entities.add(entity);
			}

			UserDAO uDao = new UserDAO();
			for (User user : entities) {
				uDao.add(user);
			}

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | DatabasePoolException e) {
			Server.logger.errorLogEntry(e);
		}

	}

	public void importFromOldStructure() {
		importFromOldStructure(true);
	}

	public void importFromOldStructure(boolean showConsoleOutput) {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", EnvConst.OLD_STRUCTDB_USER);
		connectionProps.put("password", EnvConst.OLD_STRUCTDB_PWD);

		try {
			conn = DriverManager.getConnection(EnvConst.OLD_STRUCTDB_URL, connectionProps);
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			PreparedStatement pst;
			// ResultSet rs = st.executeQuery("select * from employers");
			// ResultSet rs =
			// st.executeQuery("select * from employers where obl in (select
			// docid from maindocs)");
			ResultSet rs = st.executeQuery("select * from employers");
			conn.commit();
			EmployeeDAO dao = new EmployeeDAO(new _Session(env, new AnonymousUser()));
			OrganizationDAO orgDAO = new OrganizationDAO(new _Session(env, new AnonymousUser()));
			ApplicationDAO aDao = new ApplicationDAO(new _Session(env, new AnonymousUser()));
			RoleDAO roleDAO = new RoleDAO(new _Session(env, new AnonymousUser()));
			PositionDAO posDAO = new PositionDAO(new _Session(env, new AnonymousUser()));
			OrganizationLabelDAO orgLabDAO = new OrganizationLabelDAO(new _Session(env, new AnonymousUser()));
			UserDAO uDao = new UserDAO();
			Employee entity;
			while (rs.next()) {
				/*----------------------------------------*/
				String login = rs.getString("userid");
				User user = (User) uDao.findByLogin(login);
				if (user != null) {
					entity = dao.findByUserId(user.getId());
					if (entity == null) {
						if (dao.findAllByName(rs.getString("fullname"), 0, 50).getCount() == 0) {
							entity = new Employee();
							entity.setName(rs.getString("fullname"));

							PreparedStatement ps_orgs = conn.prepareStatement("select * from maindocs where viewtext = ?");
							ps_orgs.setString(1, rs.getString("shortname"));
							ResultSet rs_orgs = ps_orgs.executeQuery();
							if (rs_orgs.next()) {
								PreparedStatement ps_cf = conn
								        .prepareStatement("select value from custom_fields where docid = ? and value <> '' and name = 'bin'");
								ps_cf.setInt(1, rs_orgs.getInt("docid"));
								ResultSet rs_cf = ps_cf.executeQuery();
								if (rs_cf.next()) {
									ViewPage<Organization> orgPage = orgDAO.findAllequal("name", rs_orgs.getString("viewtext"), 0, 100);
									if (orgPage != null && orgPage.getCount() > 0) {
										entity.setOrganization(orgPage.getResult().get(0));
									} else {
										Organization newOrg = new Organization();
										newOrg.setName(rs_orgs.getString("viewtext"));
										newOrg.setRegDate(new java.util.Date());
										// newOrg.setOrgCategory(new
										// OrgCategory());
										newOrg.setLabels(new ArrayList<>());
										newOrg.setRank(999);
										String bin = rs_cf.getString("value").replaceAll(" ", "");
										if (bin.length() > 12) {
											bin = bin.substring(0, 11);
										}
										newOrg.setBin(bin);
										newOrg.setForm("organization-form");

										OrganizationLabel orgCategories = orgLabDAO.findByName("balance_holder");
										if (orgCategories != null) {
											ArrayList<OrganizationLabel> labels = new ArrayList<>();
											labels.add(orgCategories);
											newOrg.setLabels(labels);
										}

										try {
											orgDAO.add(newOrg);
											entity.setOrganization(newOrg);
										} catch (SecureException e) {
											e.printStackTrace();
										}
									}
								}
							}

							// entity.setPosition(posDAO.findById(UUID.fromString(formData.getValue("position"))));
							pst = conn.prepareStatement("select * from user_roles where empid = ? and type = 889");
							pst.setInt(1, rs.getInt("empid"));
							ResultSet rs_roles = pst.executeQuery();
							conn.commit();
							List<Role> roleList = new ArrayList<>();
							while (rs_roles.next()) {
								if (rs_roles.getString("name").equalsIgnoreCase("administrator")) {
									if (rs_roles.getString("appid").equalsIgnoreCase("Glossary")) {
										List<Role> role = roleDAO.findAllequal("name", "reference_admin", 0, 50).getResult();
										roleList.addAll(role);
									}
									if (rs_roles.getString("appid").equalsIgnoreCase("Structure")) {
										List<Role> role = roleDAO.findAllequal("name", "staff_admin", 0, 50).getResult();
										roleList.addAll(role);
									}
								}
							}

							Position tmpPos = posDAO.findByName("unknown");
							entity.setPosition(tmpPos);

							if (!roleList.isEmpty()) {
								entity.setRoles(roleList);
							}

							List<Application> appList = new ArrayList<Application>();
							appList.add(aDao.findByName("MunicipalProperty"));
							appList.add(aDao.findByName("Accountant"));
							appList.add(aDao.findByName("PropertyLeasing"));
							appList.add(aDao.findByName("Registry"));
							user.setAllowedApps(appList);
							entity.setUser(user);

							dao.add(entity);
						}
					}
				}
				/*----------------------------------------*/
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
