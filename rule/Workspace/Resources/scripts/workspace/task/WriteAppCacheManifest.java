package workspace.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.env.EnvConst;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.constants.Trigger;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.server.Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Command(name = "wr_app_cache_manifest", trigger = Trigger.POST_APP_START)
public class WriteAppCacheManifest extends _Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        String v = EnvConst.SERVER_VERSION + "-" + System.currentTimeMillis();
        String manifestFilePath = appEnv.getWebAppsPath() + File.separator + "manifest.appcache";

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(manifestFilePath))) {
            writer.write("CACHE MANIFEST\n");
            writer.write("# version " + v + "\n");
            writer.write("\n");
            writer.write("CACHE:\n");
            writer.write("css/all.min.css?v=" + v + "\n");
            writer.write("img/favicon.png?v=" + v + "\n");
            writer.write("img/logo.png?v=" + v + "\n");
            writer.write("img/loading.gif?v=" + v + "\n");
            writer.write("/SharedResources/ng-app/vendor.js.gz?v=" + v + "\n");
            writer.write("/SharedResources/ng-app/app.js.gz?v=" + v + "\n");
            writer.write("/SharedResources/vendor/bootstrap/css/bootstrap.min.css\n");
            writer.write("/SharedResources/vendor/font-awesome/css/font-awesome.min.css?v=4.7.0\n");
            writer.write("/SharedResources/vendor/font-awesome/fonts/fontawesome-webfont.woff2?v=4.7.0\n");
            writer.write("\n");
            writer.write("NETWORK:\n");
            writer.write("*\n");
        } catch (IOException e) {
            Server.logger.errorLogEntry(e);
        }
    }
}
