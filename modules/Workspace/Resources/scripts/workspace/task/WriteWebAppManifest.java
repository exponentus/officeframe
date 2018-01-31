package workspace.task;

import administrator.model.WebAppManifest;
import com.exponentus.appenv.AppEnv;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.constants.Trigger;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.server.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import workspace.init.ModuleConst;

import java.io.File;
import java.io.IOException;

@Command(name = ModuleConst.CODE + "_wr_web_app_manifest", trigger = Trigger.POST_APP_START)
public class WriteWebAppManifest extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        String manifestFilePath = appEnv.getWebAppsPath() + File.separator + "manifest.json";

        WebAppManifest manifest = new WebAppManifest();

        ObjectMapper om = new ObjectMapper();
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(new File(manifestFilePath), manifest);
        } catch (IOException e) {
            Server.logger.exception(e);
        }
    }
}
