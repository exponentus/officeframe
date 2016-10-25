package staff.page.form;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;
import staff.dao.OrganizationDAO;
import staff.dao.OrganizationLabelDAO;
import staff.model.Organization;
import staff.model.OrganizationLabel;

/**
 * @author Kayra created 09-01-2016
 */

public class OrganizationForm extends StaffForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		Organization entity;
		if (!id.isEmpty()) {
			OrganizationDAO dao = new OrganizationDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = new Organization();
			entity.setAuthor(user);
			entity.setName("");
			entity.setBin("");
			OrgCategory oc = new OrgCategory();
			oc.setName("");
			entity.setOrgCategory(oc);
			entity.setLabels(new ArrayList<>());

		}
		addContent(entity);
		addContent(new _POJOListWrapper<>(new OrganizationLabelDAO(session).findAll(), session));
		addContent(getSimpleActionBar(session, session.getLang()));

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
			OrganizationDAO dao = new OrganizationDAO(session);
			Organization entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Organization();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setLocalizedName(getLocalizedNames(session, formData));
			OrgCategoryDAO ocDao = new OrgCategoryDAO(session);
			entity.setOrgCategory(ocDao.findById(formData.getValue("orgcategory")));
			entity.setBin(formData.getValue("bin"));
			OrganizationLabelDAO olDao = new OrganizationLabelDAO(session);
			List<OrganizationLabel> labels = new ArrayList<>();
			for (String labelId : formData.getListOfValuesSilently("labels")) {
				if (!labelId.isEmpty()) {
					OrganizationLabel prgLabel = olDao.findById(labelId);
					if (prgLabel != null) {
						labels.add(prgLabel);
					}
				}
			}
			entity.setLabels(labels);

			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}

		} catch (_Exception | DatabaseException | SecureException | DAOException e) {
			logError(e);
		}
	}

	@Override
	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();

		if (formData.getValueSilently("name").isEmpty()) {
			ve.addError("name", "required", getLocalizedWord("field_is_empty", lang));
		}
		if (formData.getValueSilently("orgcategory").isEmpty()) {
			ve.addError("orgcategory", "required", getLocalizedWord("field_is_empty", lang));
		}
		/*
		 * if (formData.getValueSilently("bin").isEmpty()) { ve.addError("bin",
		 * "required", getLocalizedWord("field_is_empty", lang)); } else if
		 * (formData.getValueSilently("bin").length() != 12) {
		 * ve.addError("bin", "eq_12",
		 * getLocalizedWord("bin_value_should_be_consist_from_12_symbols",
		 * lang)); }
		 */
		if (formData.getValueSilently("bin").length() != 12 && formData.getValueSilently("bin").length() > 0) {
			ve.addError("bin", "eq_12", getLocalizedWord("bin_value_should_be_consist_from_12_symbols", lang));
		}

		return ve;
	}
}
