package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.WayOfInteractionDAO;
import reference.init.ModuleConst;
import reference.model.WayOfInteraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_wayofinteractions")
public class FillWayOfInteractions extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<WayOfInteraction> entities = new ArrayList<WayOfInteraction>();
        String[] data = {"phone_call", "e_mail", "orally_in_person"};
        String[] dataEng = {"Phone call", "E-mail", "Orally, in person"};
        String[] dataRus = {"Телефонный звонок", "Электронная почта", "Устно, при личной встрече"};
        String[] dataKaz = {"Телефон қоңырауы", "Электрондық пошта", "Ауызша, жеке тұлға"};

        for (int i = 0; i < data.length; i++) {
            WayOfInteraction entity = new WayOfInteraction();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            WayOfInteractionDAO dao = new WayOfInteractionDAO(ses);
            for (WayOfInteraction entry : entities) {
                try {
                    if (dao.add(entry) != null) {
                        logger.info(entry.getName() + " added");
                    }
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
            }
        } catch (DAOException e) {
            logger.exception(e);
        }
        logger.info("done...");
    }

}
