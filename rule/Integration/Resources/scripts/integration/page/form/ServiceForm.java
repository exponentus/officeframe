package integration.page.form;

import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoForm;

public class ServiceForm extends _DoForm {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		addValue("getbycoord", "/" + getCurrentAppEnv().appName + "/rest/gis/getbycoord/0");
		addValue("getbystreet", "/" + getCurrentAppEnv().appName + "/rest/gis/getbystreet/0/0");
		_ActionBar actionBar = new _ActionBar(session, getCurrentAppEnv());
		actionBar.addAction(new Action("Close", "just close the form", _ActionType.CLOSE));
		addContent(actionBar);
	}
}
