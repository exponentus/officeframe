package staff.page.view;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;
import staff.dao.EmployeeDAO;
import staff.model.Employee;

import java.util.UUID;

/**
 * @author Kayra created 07-01-2016
 */

public class EmployeeView extends _DoPage {

    @Override
    public void doGET(_Session session, _WebFormData formData) {
        LanguageCode lang = session.getLang();
        IUser<Long> user = session.getUser();
        try {
            if (user.getId() == SuperUser.ID || user.getRoles().contains("staff_admin")) {
                _ActionBar actionBar = new _ActionBar(session);
                _Action newDocAction = new _Action(getLocalizedWord("new_", lang), "", "new_employee");
                newDocAction.setURL("Provider?id=employee-form");
                actionBar.addAction(newDocAction);
                actionBar.addAction(
                        new _Action(getLocalizedWord("del_document", lang), "", _ActionType.DELETE_DOCUMENT));
                addContent(actionBar);
            }
            addContent(getViewPage(new EmployeeDAO(session), formData));
        } catch (DAOException e) {
            logError(e);
            setBadRequest();
        }
    }

    @Override
    public void doDELETE(_Session session, _WebFormData formData) {
        println(formData);
        try {
            IUser<Long> user = session.getUser();
            if (user.getId() == SuperUser.ID || user.getRoles().contains("staff_admin")) {
                EmployeeDAO dao = new EmployeeDAO(session);
                for (String id : formData.getListOfValuesSilently("docid")) {
                    Employee m = dao.findById(UUID.fromString(id));
                    try {
                        dao.delete(m);
                    } catch (SecureException e) {
                        setError(e);
                    }
                }
            }
        } catch (DAOException e) {
            logError(e);
            setBadRequest();
        }
    }
}
