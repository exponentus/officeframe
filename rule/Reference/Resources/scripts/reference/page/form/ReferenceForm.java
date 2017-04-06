package reference.page.form;

import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
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
		IUser<Long> user = ses.getUser();
		if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
			actionBar.addAction(new _Action(getLocalizedWord("save_close", lang), "", _ActionType.SAVE_AND_CLOSE));
		}
		actionBar.addAction(new _Action(getLocalizedWord("close", lang), "", _ActionType.CLOSE));
		return actionBar;

	}

}
