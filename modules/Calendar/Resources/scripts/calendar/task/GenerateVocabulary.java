package calendar.task;

import calendar.init.ModuleConst;
import com.exponentus.appenv.AppEnv;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.constants.Trigger;
import com.exponentus.scriptprocessor.tasks.Command;

@Command(name = ModuleConst.CODE + "_gen_voc", trigger = Trigger.POST_APP_START)
public class GenerateVocabulary extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        generateVocabulary(appEnv);
    }
}
