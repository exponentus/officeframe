package staff.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.constants.Trigger;
import com.exponentus.scriptprocessor.tasks.Command;
import staff.init.AppConst;

@Command(name = AppConst.CODE + "_gen_voc", trigger = Trigger.POST_APP_START)
public class GenerateVocabulary extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        generateVocabulary(appEnv);
    }
}
