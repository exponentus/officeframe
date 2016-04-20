package reference.page.form;

import java.util.Date;

import com.exponentus.common.model.SimpleEntity;
import com.exponentus.common.page.form.Form;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;

import kz.nextbase.script.actions._Action;
import kz.nextbase.script.actions._ActionBar;
import kz.nextbase.script.actions._ActionType;

/**
 * @author Kayra created 03-01-2016
 */

public abstract class ReferenceForm extends Form {

	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();
		if (formData.getValueSilently("name").isEmpty()) {
			ve.addError("name", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}

	protected IOutcomeObject getSimpleActionBar(_Session ses) {
		_ActionBar actionBar = new _ActionBar(ses);
		LanguageCode lang = ses.getLang();
		IUser<Long> user = ses.getUser();
		if (user.getId() == SuperUser.ID || user.getRoles().contains("reference_admin")) {
			actionBar.addAction(new _Action(getLocalizedWord("save_close", lang), "", _ActionType.SAVE_AND_CLOSE));
		}
		actionBar.addAction(new _Action(getLocalizedWord("close", lang), "", _ActionType.CLOSE));
		return actionBar;

	}

	protected SimpleEntity getDefaultEntity(IUser<Long> user, SimpleEntity entity) {
		entity.setAuthor(user);
		entity.setRegDate(new Date());
		entity.setName("");
		return entity;
	}

	@Override
	public abstract void doGET(_Session session, _WebFormData formData);

	@Override
	public abstract void doPOST(_Session session, _WebFormData formData);

	@Override
	public void doPUT(_Session session, _WebFormData formData) {

	}

	@Override
	public void doDELETE(_Session session, _WebFormData formData) {

	}
}
