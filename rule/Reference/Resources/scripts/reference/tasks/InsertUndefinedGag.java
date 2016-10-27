package reference.tasks;

import java.util.HashMap;
import java.util.Map;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPatch;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.DepartmentTypeDAO;
import reference.dao.OrgCategoryDAO;
import reference.model.DepartmentType;
import reference.model.OrgCategory;

@Command(name = "prepare_ref")
public class InsertUndefinedGag extends _DoPatch {
	public static String gagKey = "undefined";

	@Override
	public void doTask(_Session ses) {
		Map<LanguageCode, String> gag = new HashMap<>();
		gag.put(LanguageCode.ENG, "Undefined");
		gag.put(LanguageCode.KAZ, "Неопределенно");
		gag.put(LanguageCode.RUS, "Неопределенно");

		DepartmentTypeDAO dao = new DepartmentTypeDAO(ses);
		DepartmentType entity = new DepartmentType();
		entity.setName(gagKey);
		entity.setLocalizedName(gag);
		try {
			dao.add(entity);
		} catch (SecureException | DAOException e) {
			logger.errorLogEntry(e);
		}

		OrgCategoryDAO dao1 = new OrgCategoryDAO(ses);
		OrgCategory entity1 = new OrgCategory();
		entity1.setName(gagKey);
		entity1.setLocalizedName(gag);
		try {
			dao1.add(entity1);
		} catch (SecureException | DAOException e) {
			logger.errorLogEntry(e);
		}

	}

}
