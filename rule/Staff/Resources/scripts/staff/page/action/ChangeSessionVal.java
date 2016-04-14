package staff.page.action;

import kz.flabs.util.Util;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;


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
