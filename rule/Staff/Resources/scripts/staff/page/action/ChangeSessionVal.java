package staff.page.action;

import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

public class ChangeSessionVal extends _DoPage {
    @Override
    public void doPOST(_Session session, _WebFormData formData) {
        int pageSize = formData.getNumberValueSilently("pagesize", 0);
        String lang = formData.getValueSilently("lang");

        if (pageSize > 9 && pageSize < 1001) {
            session.pageSize = pageSize;
        }
        if (!lang.isEmpty()) {
            session.setLang(LanguageCode.valueOf(lang));
        }
    }
}
