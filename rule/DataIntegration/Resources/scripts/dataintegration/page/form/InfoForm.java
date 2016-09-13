package dataintegration.page.form;

import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoForm;

public class InfoForm extends _DoForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		addValue("getbycoord", "/" + session.getAppEnv().appName + "/rest/gis/getbycoord/0");
		addValue("getbystreet", "/" + session.getAppEnv().appName + "/rest/gis/getbystreet/0/0");
		_ActionBar actionBar = new _ActionBar(session);
		actionBar.addAction(new _Action("Close", "just close the form", _ActionType.CLOSE));
		addContent(actionBar);
	}
}
