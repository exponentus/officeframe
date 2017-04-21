package staff.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.exponentus.appenv.AppEnv;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.fasterxml.jackson.databind.ObjectMapper;

import administrator.dao.UserDAO;
import administrator.model.User;
import reference.model.DepartmentType;
import reference.model.OrgCategory;
import reference.model.Position;
import staff.model.Department;
import staff.model.Employee;
import staff.model.Organization;

@Command(name = "backup_of")
public class BackupOfficeframe extends _Do {
	private ObjectMapper mapper = new ObjectMapper();

	public void doTask(AppEnv appEnv, _Session ses) {
		backupUser();
		backup(ses, OrgCategory.class);
		backup(ses, DepartmentType.class);
		backup(ses, Position.class);
		backup(ses, Organization.class);
		backup(ses, Department.class);
		backup(ses, Employee.class);
	}

	private void backup(_Session ses, Class class1) {
		FileWriter fileWriter = getFile(class1);
		try {
			IDAO<?, UUID> dao = DAOFactory.get(ses, class1.getCanonicalName());
			fileWriter.append(mapper.writeValueAsString(dao.findAll().getResult()));
			System.out.println(class1.getName() + " backup was created successfully");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void backupUser() {
		FileWriter fileWriter = getFile(User.class);
		try {
			UserDAO dao = new UserDAO();

			fileWriter.append(mapper.writeValueAsString(dao.findAll()));
			System.out.println(User.class.getName() + " backup was created successfully");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private FileWriter getFile(Class<?> class1) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(EnvConst.BACKUP_DIR + File.separator + class1.getName() + "_.json");
			return fileWriter;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
