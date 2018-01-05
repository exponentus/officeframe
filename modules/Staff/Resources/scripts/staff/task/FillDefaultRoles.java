package staff.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import staff.init.ModuleConst;

@Command(name = ModuleConst.CODE + "_fill_roles")
public class FillDefaultRoles extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        addApplicationRoles(appEnv);
    }

}
