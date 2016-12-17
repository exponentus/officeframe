package reference.task;

import java.util.HashMap;
import java.util.Map;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.legacy.ConvertorEnvConst;
import com.exponentus.localization.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.DepartmentTypeDAO;
import reference.dao.DocumentLanguageDAO;
import reference.dao.OrgCategoryDAO;
import reference.model.DepartmentType;
import reference.model.DocumentLanguage;
import reference.model.OrgCategory;

@Command(name = "prepare_storage")
public class InsertUndefinedGag extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Vocabulary vocabular = getCurrentAppEnv().vocabulary;

		Map<LanguageCode, String> gag = new HashMap<>();
		gag.put(LanguageCode.ENG, vocabular.getWord("undefined", LanguageCode.ENG));
		gag.put(LanguageCode.KAZ, vocabular.getWord("undefined", LanguageCode.KAZ));
		gag.put(LanguageCode.RUS, vocabular.getWord("undefined", LanguageCode.RUS));
		try {
			DepartmentTypeDAO dao = new DepartmentTypeDAO(ses);
			DepartmentType entity = new DepartmentType();
			entity.setName(ConvertorEnvConst.GAG_KEY);
			entity.setLocalizedName(gag);
			try {
				dao.add(entity);
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
				} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
					logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
				} else {
					logger.errorLogEntry(e);
				}
			} catch (SecureException e) {
				logger.errorLogEntry(e);
			}
			
			OrgCategoryDAO dao1 = new OrgCategoryDAO(ses);
			OrgCategory entity1 = new OrgCategory();
			entity1.setName(ConvertorEnvConst.GAG_KEY);
			entity1.setLocalizedName(gag);
			try {
				dao1.add(entity1);
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
				} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
					logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
				} else {
					logger.errorLogEntry(e);
				}
			} catch (SecureException e) {
				logger.errorLogEntry(e);
			}
			
			DocumentLanguageDAO dao2 = new DocumentLanguageDAO(ses);
			DocumentLanguage entity2 = new DocumentLanguage();
			entity2.setName(ConvertorEnvConst.GAG_ENTITY);
			entity2.setCode(LanguageCode.UNKNOWN);
			entity2.setLocalizedName(gag);
			try {
				dao2.add(entity2);
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
				} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
					logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
				} else {
					logger.errorLogEntry(e);
				}
			} catch (SecureException e) {
				logger.errorLogEntry(e);
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
		}
	}

}
