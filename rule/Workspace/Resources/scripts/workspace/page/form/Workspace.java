package workspace.page.form;

import java.util.Collections;
import java.util.List;

import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.server.Server;
import com.exponentus.user.AnonymousUser;
import com.exponentus.user.IUser;

import administrator.dao.ApplicationDAO;
import administrator.dao.LanguageDAO;
import administrator.model.Application;

public class Workspace extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) throws _Exception {
		addValue("serverversion", Server.serverVersion);
		addValue("build", Server.compilationTime);
		addValue("org", Environment.orgName);

		if (!session.getUser().getUserID().equalsIgnoreCase(AnonymousUser.USER_NAME)) {
			IUser<Long> user = session.getUser();
			List<Application> aa = user.getAllowedApps();
			ApplicationDAO aDao = new ApplicationDAO(session);
			Application app = aDao.findByName(EnvConst.WORKSPACE_NAME);
			if (app != null) {
				aa.remove(app);
			}
			Collections.sort(aa, (left, right) -> left.getPosition() - right.getPosition());
			addContent(aa);
		} else {
			setUnauthorized();
		}
		addContent(new LanguageDAO(session).findAll());
	}
}
