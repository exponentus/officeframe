package reference.page.form;

import administrator.dao.LanguageDAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;
import org.eclipse.persistence.exceptions.DatabaseException;
import reference.dao.DemandTypeDAO;
import reference.model.DemandType;

public class DemandTypeForm extends ReferenceForm {

    @Override
    public void doGET(_Session session, _WebFormData formData) {
        String id = formData.getValueSilently("docid");
        IUser<Long> user = session.getUser();
        DemandType entity;
        if (!id.isEmpty()) {
            DemandTypeDAO dao = new DemandTypeDAO(session);
            entity = dao.findById(id);
        } else {
            entity = (DemandType) getDefaultEntity(user, new DemandType());
            entity.setPrefix("");
        }
        addContent(entity);
        addContent(new LanguageDAO(session).findAll());
        addContent(getSimpleActionBar(session));
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

            String id = formData.getValueSilently("docid");
            DemandTypeDAO dao = new DemandTypeDAO(session);
            DemandType entity;
            boolean isNew = id.isEmpty();

            if (isNew) {
                entity = new DemandType();
            } else {
                entity = dao.findById(id);
            }

            entity.setName(formData.getValue("name"));
            entity.setPrefix(formData.getValue("prefix"));
            entity.setLocalizedName(getLocalizedNames(session, formData));

            if (isNew) {
                dao.add(entity);
            } else {
                dao.update(entity);
            }

        } catch (_Exception | DatabaseException | SecureException | DAOException e) {
            logError(e);
        }
    }

    protected _Validation validate(_WebFormData formData, LanguageCode lang) {
        _Validation ve = simpleCheck("name");
        return ve;
    }
}
