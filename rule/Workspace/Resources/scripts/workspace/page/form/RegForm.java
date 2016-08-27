package workspace.page.form;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.env.Environment;
import com.exponentus.exception.MsgException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.messaging.email.MailAgent;
import com.exponentus.messaging.email.Memo;
import com.exponentus.scripting._AppEntourage;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import administrator.dao.LanguageDAO;
import administrator.dao.UserDAO;
import administrator.model.User;

public class RegForm extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		_AppEntourage ent = session.getAppEntourage();
		addValue("serverversion", ent.getServerVersion());
		addValue("build", ent.getBuildTime());
		addValue("org", Environment.orgName);
		addValue("appname", ent.getAppName());

		String lang = formData.getValueSilently("lang");
		if (!lang.isEmpty()) {
			session.setLang(LanguageCode.valueOf(lang));
		}

		addContent(new LanguageDAO(session).findAll());
	}

	@Override
	public void doPOST(_Session session, _WebFormData formData) {
		try {
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String email = formData.getValueSilently("email");
			String fio = formData.getValueSilently("fio");
			String org = formData.getValueSilently("org");
			String orgbin = formData.getValueSilently("orgbin");
			String login = formData.getValueSilently("login");
			String comment = formData.getValueSilently("comment");

			UserDAO dao = new UserDAO(session);
			List<String> recipients = new ArrayList<String>();
			List<User> list = dao.findAllAdministrators(1, 100).getResult();

			for (User user : list) {
				recipients.add(user.getEmail());
			}

			MailAgent ma = new MailAgent();
			Memo memo = new Memo();
			if (!ma.sendMеssage(recipients, "registration request",
			        memo.getBody(fio + " " + org + "  " + orgbin + "  " + login + " " + email + " " + comment))) {
				addContent("notify", "ok");
			}

		} catch (MsgException e) {
			logError(e);
		} catch (Exception e) {
			logError(e);
		}
	}

	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();

		if (formData.getValueSilently("email").isEmpty()) {
			ve.addError("email", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("fio").isEmpty()) {
			ve.addError("fio", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("org").isEmpty()) {
			ve.addError("org", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (!formData.getValueSilently("orgbin").isEmpty()) {
			if (formData.getValueSilently("orgbin").length() != 12) {
				ve.addError("orgbin", "eq_12", getLocalizedWord("bin_value_should_be_consist_from_12_symbols", lang));
			}
		}

		return ve;
	}
}
