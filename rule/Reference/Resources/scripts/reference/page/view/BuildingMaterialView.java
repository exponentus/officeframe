package reference.page.view;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;

import reference.dao.BuildingMaterialDAO;
import reference.model.BuildingMaterial;

public class BuildingMaterialView extends ReferenceView<BuildingMaterial> {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.getId() == SuperUser.ID || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "",
						"new_building_material");
				newDocAction.setURL("p?id=buildingmaterial-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new _Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}
			addContent(getViewPage(new BuildingMaterialDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			
		}
	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		delete(formData.getListOfValuesSilently("docid"), BuildingMaterial.class);
	}
}
