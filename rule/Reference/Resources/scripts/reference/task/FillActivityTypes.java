package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.ActivityTypeDAO;
import reference.init.AppConst;
import reference.model.ActivityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_activity_types")
public class FillActivityTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<ActivityType> entities = new ArrayList<>();

        String names[] = {"non_ferrous_metals_and_black_metals", "pesticides", "medical_activities",
                "pharmaceutical_activities", "activities_in_the_field_of_veterinary_medicine"};
        String namesEng[] = {"Non-ferrous metals and black metals", "Pesticides (pesticides)", "Medical activities",
                "Pharmaceutical activities", "Activities in the field of veterinary medicine"};
        String namesRus[] = {"Түсті және қара металдар", "Пестицидтер", "Медициналық қызмет",
                "Фармацевтикалық қызмет", "Ветеринариялық медицина саласындағы қызмет"};
        for (int i = 0; i < names.length; i++) {
            ActivityType entity = new ActivityType();
            entity.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            ActivityTypeDAO dao = new ActivityTypeDAO(ses);
            for (ActivityType entry : entities) {
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
