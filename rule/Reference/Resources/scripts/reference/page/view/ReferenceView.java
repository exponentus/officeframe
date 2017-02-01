package reference.page.view;

import java.util.UUID;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.exception.SecureException;
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.event._DoPage;

public class ReferenceView extends _DoPage {

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		try {
			IDAO<IAppEntity, UUID> dao = (IDAO<IAppEntity, UUID>) DAOFactory.get(getSes(), formData.getValue("form1"));
			for (String id : formData.getListOfValuesSilently("docid")) {
				IAppEntity m = dao.findById(UUID.fromString(id));
				dao.delete(m);
			}
		} catch (DAOException e) {
			if (e.getType() == DAOExceptionType.VIOLATES_FOREIGN_KEY) {
				_Validation ve = new _Validation();
				ve.addError("", DAOExceptionType.VIOLATES_FOREIGN_KEY.name(),
						getLocalizedWord(DAOExceptionType.VIOLATES_FOREIGN_KEY.name(), getSes().getLang()));
				setBadRequest();
				setValidation(ve);
			} else {
				logError(e);
			}
		} catch (SecureException | WebFormException e) {
			logError(e);
			setBadRequest();
		}
	}

}
