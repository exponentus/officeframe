package staff.page.action;

import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;

public class ChangeSessionVal extends _DoPage {
	@Override
	public void doPOST(_Session session, WebFormData formData) {
		int pageSize = formData.getNumberValueSilently("pagesize", 0);

		if (pageSize > 9 && pageSize < 1001) {
			session.pageSize = pageSize;
		}

	}
}
