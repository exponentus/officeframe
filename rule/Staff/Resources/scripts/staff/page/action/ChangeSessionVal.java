package staff.page.action;

import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.util.Util;

public class ChangeSessionVal extends _DoPage {
	@Override
	public void doPOST(_Session session, _WebFormData formData) {
		String pageSize = formData.getValueSilently("pagesize");
		String lang = formData.getValueSilently("lang");

		if (!pageSize.isEmpty()) {
			session.pageSize = Util.convertStringToInt(pageSize);
		}
		if (!lang.isEmpty()) {
			session.setLang(LanguageCode.valueOf(lang));
		}
	}
}
