package staff.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import staff.init.AppConst;
import staff.dao.RoleDAO;
import staff.model.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_roles")
public class FillDefaultRoles extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<Role> entities = new ArrayList<Role>();

		for (String roleName: AppConst.ROLES) {
			Role entity = new Role();
			entity.setName(roleName);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, roleName);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			RoleDAO dao = new RoleDAO(ses);
			for (Role entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.info(entry.getName() + " added");
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
		} catch (DAOException e) {
			logger.exception(e);
		}
		logger.info("done...");
	}

}
