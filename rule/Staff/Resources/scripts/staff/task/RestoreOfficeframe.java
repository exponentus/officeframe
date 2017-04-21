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
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import administrator.dao.UserDAO;
import administrator.model.User;
import administrator.model.embedded.UserApplication;
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;
import staff.dao.OrganizationDAO;
import staff.model.Organization;

@Command(name = "restore_of")
public class RestoreOfficeframe extends _Do {
	private ObjectMapper mapper = new ObjectMapper();

	public void doTask(AppEnv appEnv, _Session ses) {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		restoreUser();
		// restore(ses, OrgCategory.class);
		// restore(ses, DepartmentType.class);
		// restore(ses, Position.class);
		// restoreOrganization(ses);
		// restore(ses, Department.class);
		// restore(ses, Employee.class);
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
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
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
		// mapper.addMixIn(Organization.class, OrganizationMixIn.class);
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
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
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
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
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

	abstract class OrganizationMixIn {
		@JsonDeserialize(using = OrgCategoryDeserializer.class)
		OrgCategory orgCategory;
	}

	public class OrgCategoryDeserializer extends JsonDeserializer<OrgCategory> {

		@Override
		public OrgCategory deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {
			JsonNode node = jsonParser.getCodec().readTree(jsonParser);
			String name = node.get("name").asText();
			OrgCategoryDAO dao = (OrgCategoryDAO) DAOFactory.get(new _Session(new SuperUser()), "orgcategory");
			try {
				return dao.findByName(name);
			} catch (DAOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
