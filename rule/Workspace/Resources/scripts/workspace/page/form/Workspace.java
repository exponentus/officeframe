package workspace.page.form;

import java.util.List;

import com.exponentus.env.Environment;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._AppEntourage;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.AnonymousUser;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import administrator.model.Application;
import kz.nextbase.script._Exception;

public class Workspace extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) throws _Exception {
		_AppEntourage ent = session.getAppEntourage();
		addValue("serverversion", ent.getServerVersion());
		addValue("build", ent.getBuildTime());
		addValue("org", Environment.orgName);
		addValue("appname", ent.getAppName());

		String lang = formData.getValueSilently("lang");
		if (!lang.isEmpty()) {
			session.setLang(LanguageCode.valueOf(lang));
		}

		if (!session.getUser().getUserID().equalsIgnoreCase(AnonymousUser.USER_NAME)) {
			IUser<Long> user = session.getUser();
			List<Application> aa = user.getAllowedApps();
			addContent(new _POJOListWrapper(aa, session));
		}
		addContent(new _POJOListWrapper(new LanguageDAO(session).findAll(), session));
	}
}
