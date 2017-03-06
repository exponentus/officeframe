package reference.task;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.appenv.AppEnv;
import com.exponentus.localization.Vocabulary;
import com.exponentus.localization.VocabularyDTO;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.constants.Trigger;
import com.exponentus.scriptprocessor.tasks.Command;

import java.io.File;

@Command(name = "ref_gen_voc", trigger = Trigger.POST_APP_START)
public class GenerateVocabulary extends _Do {
	
	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		Vocabulary vocabulary = appEnv.vocabulary;
		
		String i18nPath = appEnv.getWebAppsPath() + File.separator + "i18n" + File.separator;
		(new File(i18nPath)).mkdirs();
		LanguageDAO dao = new LanguageDAO();
		for (Language lang : dao.findAllActivated()) {
			VocabularyDTO vocabularyDTO = VocabularyDTO.valueOf(vocabulary, lang.getCode());
			String jsonFilePath = i18nPath + lang.getName().toLowerCase() + ".json";
			//logger.infoLogEntry("write to \"" + jsonFilePath + "\"");
			writeJS(vocabularyDTO.getWords(), jsonFilePath);
		}
		
		//logger.infoLogEntry("done...");
	}
}
