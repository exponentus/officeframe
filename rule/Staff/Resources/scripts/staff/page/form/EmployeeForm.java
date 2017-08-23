package staff.page.form;

import administrator.dao.UserDAO;
import administrator.model.User;
import com.exponentus.common.model.Attachment;
import com.exponentus.common.model.embedded.Avatar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.rest.stream.IAppFile;
import com.exponentus.rest.stream.TempFile;
import com.exponentus.scripting.*;
import com.exponentus.server.Server;
import com.exponentus.user.IUser;
import org.apache.commons.io.IOUtils;
import reference.dao.PositionDAO;
import reference.model.Position;
import staff.dao.DepartmentDAO;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Organization;
import staff.model.Role;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Kayra created 07-01-2016
 */

public class EmployeeForm extends StaffForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser user = session.getUser();
		String fsId = formData.getValueSilently(EnvConst.FSID_FIELD_NAME);
		Employee entity;

		try {
			if (!id.isEmpty()) {
				EmployeeDAO dao = new EmployeeDAO(session);
				entity = dao.findById(UUID.fromString(id));
				if (formData.containsField("avatar")) {
					_FormAttachments formFiles = session.getFormAttachments(fsId);
					Map<String, TempFile> attsMap = formFiles.getFieldFile("avatar");
					if (attsMap == null) {
						IAppFile att = entity.getAvatar();
						byte[] image = null;
						att = entity.getAvatar();
						if (att == null) {
							image = getEmptyAvatar();
						} else {
							image = att.getFile();
						}
						if (showAttachment(image)) {
							return;
						} else {
							setBadRequest();
						}
					} else {
						IAppFile att = attsMap.values().iterator().next();
						TempFile tempFile = (TempFile) att;
						showFile(tempFile.getPath(), tempFile.getRealFileName());
						return;
					}

				}
			} else {
				if (formData.containsField("avatar")) {
					_FormAttachments formFiles = session.getFormAttachments(fsId);
					Map<String, TempFile> attsMap = formFiles.getFieldFile("avatar");
					if (attsMap != null) {
						IAppFile att = attsMap.values().iterator().next();
						TempFile tempFile = (TempFile) att;
						showFile(tempFile.getPath(), tempFile.getRealFileName());
						return;
					}
					if (attsMap == null) {
						showAttachment(getEmptyAvatar());
					}
					return;
				}
				entity = new Employee();
				entity.setAuthor(user);
				entity.setName("");
				Organization tmpOrg = new Organization();
				tmpOrg.setName("");
				entity.setOrganization(tmpOrg);
				Position tmpPos = new Position();
				tmpPos.setName("");
				entity.setPosition(tmpPos);
				String roleId = formData.getValueSilently("categoryid");
				if (!roleId.isEmpty()) {
					RoleDAO rDao = new RoleDAO(session);
					Role role = rDao.findByIdentefier(roleId);
					entity.setRoles(new ArrayList<>(Arrays.asList(role)));
				} else {
					entity.setRoles(new ArrayList<Role>());
				}
				// entity.setAvatar(getEmptyAvatar());

			}
			addContent(entity);
			addContent(getSimpleActionBar(session, session.getLang()));
			addContent(new _POJOListWrapper<>(new RoleDAO(session).findAll().getResult(), session));
			addContent(new _POJOListWrapper<>(new UserDAO(session).findAll(), session));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}

	}

	@Override
	public void doPOST(_Session session, WebFormData formData) {
		devPrint(formData);
		try {
			String id = formData.getValueSilently("docid");
			EmployeeDAO dao = new EmployeeDAO(session);
			Employee entity;
			boolean isNew = id.isEmpty();

			_Validation ve = validate(formData, session.getLang(), isNew);
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			if (isNew) {
				entity = new Employee();
			} else {
				entity = dao.findByIdentefier(id);
			}

			entity.setName(formData.getValueSilently("name"));
			entity.setIin(formData.getValueSilently("iin"));
			entity.setRank(formData.getNumberValueSilently("rank", 0));
			OrganizationDAO orgDAO = new OrganizationDAO(session);
			String orgId = formData.getValueSilently("organization");
			if (!orgId.isEmpty()) {
				Organization org = orgDAO.findByIdentefier(orgId);
				entity.setOrganization(org);
			}

			String depId = formData.getValueSilently("department");
			if (!depId.isEmpty()) {
				DepartmentDAO depDAO = new DepartmentDAO(session);
				entity.setDepartment(depDAO.findByIdentefier(depId));
			}

			PositionDAO posDAO = new PositionDAO(session);
			entity.setPosition(posDAO.findByIdentefier(formData.getValueSilently("position")));
			String[] roles = formData.getListOfValuesSilently("role");
			if (roles != null) {
				RoleDAO roleDAO = new RoleDAO(session);
				List<Role> roleList = new ArrayList<>();
				for (String roleId : roles) {
					if (!roleId.isEmpty()) {
						Role role = roleDAO.findByIdentefier(roleId);
						roleList.add(role);
					}
				}
				if (!roleList.isEmpty()) {
					entity.setRoles(roleList);
				}
			}

			String login = formData.getValueSilently("login");
			if (!login.isEmpty()) {
				UserDAO uDao = new UserDAO();
				User user = (User) uDao.findByLogin(login);
				entity.setUser(user);
			} else {
				entity.setUser(null);
			}

			String fsId = formData.getValueSilently(EnvConst.FSID_FIELD_NAME);
			_FormAttachments formFiles = session.getFormAttachments(fsId);

			Map<String, TempFile> attsMap = formFiles.getFieldFile("avatar");

			if (attsMap != null && attsMap.size() > 0) {
				TempFile att = attsMap.values().iterator().next();
				entity.setAvatar((Avatar) att.convertTo(new Avatar()));
			}
			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}

		} catch (SecureException | DAOException e) {
			setBadRequest();
			logError(e);
		}
	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		Employee entity;
		try {
			if (!id.isEmpty()) {
				EmployeeDAO dao = new EmployeeDAO(session);
				entity = dao.findById(UUID.fromString(id));
				if (formData.containsField("avatar")) {
					String fsId = formData.getValueSilently(EnvConst.FSID_FIELD_NAME);
					_FormAttachments formFiles = session.getFormAttachments(fsId);
					Map<String, TempFile> attsMap = formFiles.getFieldFile("avatar");
					if (attsMap == null) {
						IAppFile att = entity.getAvatar();
						byte[] image = null;
						att = entity.getAvatar();
						if (att == null) {
							image = getEmptyAvatar();
						} else {
							image = att.getFile();
						}
						if (showAttachment(image)) {
							return;
						} else {
							setBadRequest();
						}
					} else {
						IAppFile att = attsMap.values().iterator().next();
						TempFile tempFile = (TempFile) att;
						showFile(tempFile.getPath(), tempFile.getRealFileName());
						return;
					}

				}
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}

	protected _Validation validate(WebFormData formData, LanguageCode lang, boolean isNew) {
		_Validation ve = new _Validation();

		if (formData.getValueSilently("name").isEmpty()) {
			ve.addError("name", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("organization").isEmpty() && formData.getValueSilently("department").isEmpty()) {
			ve.addError("organization", "required", getLocalizedWord("field_is_empty", lang));
			ve.addError("department", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("position").isEmpty()) {
			ve.addError("position", "required", getLocalizedWord("field_is_empty", lang));
		}

		String login = formData.getValueSilently("login");
		if (!login.isEmpty()) {
			UserDAO uDao = new UserDAO();
			User user = (User) uDao.findByLogin(login);
			if (user == null) {
				ve.addError("login", "login", getLocalizedWord("login_has_not_been_found", lang));
			}
		} else {
			ve.addError("login", "login", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}

	@Override
	protected void addContent(IPOJOObject document) {
		_Session ses = getSes();
		List<Attachment> atts = document.getAttachments();
		_FormAttachments fa = ses.getFormAttachments(formData.getValueSilently(EnvConst.FSID_FIELD_NAME));

		Map<String, TempFile> attsMap = fa.getFieldFile("avatar");

		if (attsMap != null && attsMap.size() > 0) {
			TempFile att = attsMap.values().iterator().next();
			atts.add((Attachment) att.convertTo(new Attachment()));
		}

		_POJOObjectWrapper wrapped = new _POJOObjectWrapper(document, getSes());
		result.addObject(wrapped);
	}

	private byte[] getEmptyAvatar() {
		File file = new File(Environment.getOfficeFrameDir() + File.separator + "webapps" + File.separator + EnvConst.STAFF_MODULE_NAME
				+ File.separator + "img" + File.separator + "nophoto.png");
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			Server.logger.error(file.getAbsolutePath());
			Server.logger.exception(e);

		}

		return null;
	}
}
