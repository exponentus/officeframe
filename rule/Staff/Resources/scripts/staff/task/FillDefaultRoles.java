package staff.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.constants.Trigger;
import com.exponentus.scriptprocessor.tasks.Command;
import staff.init.AppConst;
import staff.dao.RoleDAO;
import staff.model.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_roles", trigger = Trigger.POST_APP_DEPLOYED)
public class FillDefaultRoles extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		addDefaultRoles(appEnv);
	}

}
