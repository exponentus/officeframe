package reference.page.view;

import java.util.UUID;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.event._DoPage;

public class ReferenceView<T> extends _DoPage {
	
	public void delete(String[] ids, Class clazz) {
		new DAOFactory();
		IDAO<T, UUID> dao = (IDAO<T, UUID>) DAOFactory.get(getSes(), clazz);
		try {
			for (String id : formData.getListOfValuesSilently("docid")) {
				T m = dao.findById(UUID.fromString(id));
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
		} catch (SecureException e) {
			logError(e);
			setBadRequest();
		}
	}
}
