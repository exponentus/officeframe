package staff.page.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoForm;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import administrator.model.Language;

/**
 * @author Kayra created 07-01-2016
 */

public abstract class StaffForm extends _DoForm {

	protected _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();

		if (formData.getValueSilently("name").isEmpty()) {
			ve.addError("name", "required", getLocalizedWord("required", lang));
		}

		return ve;
	}

	protected _ActionBar getSimpleActionBar(_Session ses, LanguageCode lang) {
		_ActionBar actionBar = new _ActionBar(ses);
		IUser<Long> user = ses.getUser();
		if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
			actionBar.addAction(new Action(getLocalizedWord("save_close", lang), "", _ActionType.SAVE_AND_CLOSE));
		}
		actionBar.addAction(new Action(getLocalizedWord("close", lang), "", _ActionType.CLOSE));
		return actionBar;
	}

	@Override
	protected Map<LanguageCode, String> getLocalizedNames(_Session session, WebFormData formData) {
		Map<LanguageCode, String> localizedNames = new HashMap<LanguageCode, String>();
		try {
			List<Language> langs = new LanguageDAO(session).findAllActivated();
			for (Language l : langs) {
				String ln = formData.getValueSilently(l.getCode().name().toLowerCase() + "localizedname");
				if (!ln.isEmpty()) {
					localizedNames.put(l.getCode(), ln);
				} else {
					localizedNames.put(l.getCode(), formData.getValueSilently("name"));
				}
			}
		} catch (DAOException e) {
			logError(e);
		}
		return localizedNames;
	}
}
