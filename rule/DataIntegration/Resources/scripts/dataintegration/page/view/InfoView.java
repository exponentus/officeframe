package dataintegration.page.view;

import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

/**
 * @author Kayra created 18-04-2016
 */

public class InfoView extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		int pageNum = 1;
		int pageSize = session.pageSize;
		if (formData.containsField("page")) {
			pageNum = formData.getNumberValueSilently("page", pageNum);
		}

	}
}
