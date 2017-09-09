package staff.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.exponentus.appenv.AppEnv;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import administrator.dao.UserDAO;
import administrator.model.User;
import administrator.model.embedded.UserApplication;
import reference.dao.DepartmentTypeDAO;
import reference.dao.OrgCategoryDAO;
import reference.dao.PositionDAO;
import reference.model.DepartmentType;
import reference.model.OrgCategory;
import reference.model.Position;
import staff.dao.DepartmentDAO;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.model.Department;
import staff.model.Employee;
import staff.model.Organization;

@Command(name = "restore_of")
public class RestoreOfficeframe extends Do {
	private ObjectMapper mapper = new ObjectMapper();

	public void doTask(AppEnv appEnv, _Session ses) {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		restoreUser();
		restore(ses, OrgCategory.class);
		restore(ses, DepartmentType.class);
		restore(ses, Position.class);
		restoreOrganization(ses);
		restoreDepartment(ses);
		restoreEmployee(ses);
	}

	private <T extends IAppEntity<UUID>> void restore(_Session ses, Class<T> class1) {
		BufferedReader reader = getFile(class1);
		try {
			IDAO<T, UUID> dao = DAOFactory.get(ses, class1);
			String line = "";
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				T entity = (T) mapper.readValue(line, class1);
				entity.setId(null);
				try {
					dao.add(entity);
					System.out.println("\"" + entity + "\" backup was restored");
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private <T extends IAppEntity<UUID>> void restoreOrganization(_Session ses) {
		BufferedReader reader = getFile(Organization.class);
		try {
			OrganizationDAO dao = new OrganizationDAO(ses);

			String line = "";
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				try {
					Organization entity = mapper.readValue(line, Organization.class);
					Organization newOrg = new Organization();
					newOrg.setName(entity.getName());
					newOrg.setLocName(entity.getLocName());
					newOrg.setOrgCategory(new OrgCategoryDAO(ses).findByName(entity.getOrgCategory().getName()));
					dao.add(newOrg);
					System.out.println("\"" + newOrg + "\" backup was restored");

				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private <T extends IAppEntity<UUID>> void restoreDepartment(_Session ses) {
		BufferedReader reader = getFile(Department.class);
		try {
			DepartmentDAO dao = new DepartmentDAO(ses);

			String line = "";
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				try {
					Department entity = mapper.readValue(line, Department.class);
					Department newEntity = new Department();
					newEntity.setName(entity.getName());
					newEntity.setLocName(entity.getLocName());
					newEntity.setType(new DepartmentTypeDAO(ses).findByName(entity.getType().getName()));
					List<Organization> list = new OrganizationDAO(ses)
							.findAllByKeyword(newEntity.getOrganization().getName(), 1, 1).getResult();
					if (list.size() > 0) {
						newEntity.setOrganization(list.get(1));
					}
					dao.add(newEntity);
					System.out.println("\"" + newEntity + "\" backup was restored");

				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private <T extends IAppEntity<UUID>> void restoreEmployee(_Session ses) {
		BufferedReader reader = getFile(Employee.class);
		try {
			EmployeeDAO dao = new EmployeeDAO(ses);

			String line = "";
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				try {
					Employee entity = mapper.readValue(line, Employee.class);
					Employee newEntity = new Employee();
					newEntity.setName(entity.getName());
					newEntity.setLocName(entity.getLocName());
					List<Organization> list = new OrganizationDAO(ses)
							.findAllByKeyword(newEntity.getOrganization().getName(), 1, 1).getResult();
					if (list.size() > 0) {
						newEntity.setOrganization(list.get(1));
					}
					List<Department> depList = new DepartmentDAO(ses)
							.findAllequal("name", newEntity.getDepartment().getName(), 1, 1).getResult();
					if (depList.size() > 0) {
						newEntity.setDepartment(depList.get(1));
					}
					newEntity.setPosition(new PositionDAO(ses).findByName(entity.getPosition().getName()));
					dao.add(newEntity);
					System.out.println("\"" + newEntity + "\" backup was restored");

				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void restoreUser() {
		mapper.addMixIn(User.class, UserMixIn.class);
		try {
			UserDAO dao = new UserDAO();
			TypeReference<List<User>> listType = new TypeReference<List<User>>() {
			};
			List<User> entities = mapper.readValue(
					new File(EnvConst.BACKUP_DIR + File.separator + User.class.getName() + "_.json"), listType);

			for (User entity : entities) {

				entity.setId(null);
				try {
					dao.add(entity);
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				}
			}

			System.out.println(User.class.getName() + " backup was restored");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BufferedReader getFile(Class<?> class1) {
		try {
			return new BufferedReader(
					new FileReader(EnvConst.BACKUP_DIR + File.separator + class1.getName() + "_.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	abstract class UserMixIn {
		@JsonIgnore
		UserApplication userApplications;
	}

}
