package reference.page.action;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;

import reference.dao.PositionDAO;
import reference.model.Position;

public class GetPositionsAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		try {
			String keyword = formData.getValueSilently("keyword");
			int pageNum = formData.getNumberValueSilently("page", 1);
			int pageSize = ses.getPageSize();
			PositionDAO dao = new PositionDAO(ses);
			ViewPage<Position> vp = dao.findAllByKeyword(keyword, pageNum, pageSize);
			addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum());
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
