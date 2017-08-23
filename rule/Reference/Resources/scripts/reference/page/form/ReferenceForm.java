package reference.page.form;

import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions.ActionType;
import com.exponentus.scripting.event._DoForm;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import com.exponentus.user.IUser;

/**
 * @author Kayra created 03-01-2016
 */

public abstract class ReferenceForm extends _DoForm {

	@Override
	protected IOutcomeObject getSimpleActionBar(_Session ses) {
		_ActionBar actionBar = new _ActionBar(ses);
		LanguageCode lang = ses.getLang();
		IUser user = ses.getUser();
		if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
			actionBar.addAction(new Action(getLocalizedWord("save_close", lang), "", ActionType.SAVE_AND_CLOSE));
		}
		actionBar.addAction(new Action(getLocalizedWord("close", lang), "", ActionType.CLOSE));
		return actionBar;

	}

}
