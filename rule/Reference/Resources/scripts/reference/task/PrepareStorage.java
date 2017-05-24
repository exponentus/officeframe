package reference.task;

import java.util.HashMap;
import java.util.Map;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.legacy.ConvertorEnvConst;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.localization.Vocabulary;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.DepartmentTypeDAO;
import reference.dao.DocumentLanguageDAO;
import reference.dao.OrgCategoryDAO;
import reference.model.DepartmentType;
import reference.model.DocumentLanguage;
import reference.model.OrgCategory;

@Command(name = "prepare_storage")
public class PrepareStorage extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Vocabulary vocabular = getCurrentAppEnv().vocabulary;

		Map<LanguageCode, String> gag = new HashMap<>();
		gag.put(LanguageCode.ENG, vocabular.getWord(ConvertorEnvConst.GAG_KEY, LanguageCode.ENG));
		gag.put(LanguageCode.KAZ, vocabular.getWord(ConvertorEnvConst.GAG_KEY, LanguageCode.KAZ));
		gag.put(LanguageCode.RUS, vocabular.getWord(ConvertorEnvConst.GAG_KEY, LanguageCode.RUS));
		try {
			DepartmentTypeDAO dao = new DepartmentTypeDAO(ses);
			DepartmentType entity = new DepartmentType();
			entity.setName(ConvertorEnvConst.GAG_KEY);
			entity.setLocName(gag);
			try {
				dao.add(entity);
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
				} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
					logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
				} else {
					logger.exception(e);
				}
			} catch (SecureException e) {
				logger.exception(e);
			}
			
			OrgCategoryDAO dao1 = new OrgCategoryDAO(ses);
			OrgCategory entity1 = new OrgCategory();
			entity1.setName(ConvertorEnvConst.GAG_KEY);
			entity1.setLocName(gag);
			try {
				dao1.add(entity1);
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
				} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
					logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
				} else {
					logger.exception(e);
				}
			} catch (SecureException e) {
				logger.exception(e);
			}
			
			DocumentLanguageDAO dao2 = new DocumentLanguageDAO(ses);
			DocumentLanguage entity2 = new DocumentLanguage();
			entity2.setName(ConvertorEnvConst.GAG_ENTITY);
			entity2.setCode(LanguageCode.UNKNOWN);
			entity2.setLocName(gag);
			try {
				dao2.add(entity2);
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
				} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
					logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
				} else {
					logger.exception(e);
				}
			} catch (SecureException e) {
				logger.exception(e);
			}
		} catch (DAOException e) {
			logger.exception(e);
		}
	}

}
