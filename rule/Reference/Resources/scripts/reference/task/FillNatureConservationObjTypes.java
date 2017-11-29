package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.NatureConservationObjTypeDAO;
import reference.init.AppConst;
import reference.model.NatureConservationObjType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_nature_conservation_obj_types")
public class FillNatureConservationObjTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<NatureConservationObjType> entities = new ArrayList<>();

        String names[] = {"national_park", "natural_monument", "reserve", "protected_area_with_managed_resources"};
        String namesEng[] = {"National Park", "Natural Monument", "Reserve", "Protected Area with Managed Resources"};
        String namesRus[] = {"Национальный парк", "Природный памятник", "Заказник", "Охраняемая территория с управляемыми ресурсами"};
        String namesKaz[] = {"Ұлттық парк", "Табиғи ескерткіш", "Қорық", "Басқарылатын ресурстармен қорғалатын аумақ"};

        for (int i = 0; i < names.length; i++) {
            NatureConservationObjType entity = new NatureConservationObjType();
            entity.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            NatureConservationObjTypeDAO dao = new NatureConservationObjTypeDAO(ses);
            for (NatureConservationObjType entry : entities) {
                try {
                    if (dao.add(entry) != null) {
                        logger.info(entry.getName() + " added");
                    }
                } catch (DAOException e) {
                    if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                        logger.warning("a data is already exists (" + entry.getTitle() + "), record was skipped");
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
