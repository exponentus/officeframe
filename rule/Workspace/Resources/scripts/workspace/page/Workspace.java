package workspace.page;

import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.server.Server;

public class Workspace extends _DoPage {

    @Override
    public void doGET(_Session session, WebFormData formData) {
        addValue("logo", Environment.orgLogo);
        addValue("server_version", EnvConst.SERVER_VERSION);
        addValue("build", Server.compilationTime);
    }
}
