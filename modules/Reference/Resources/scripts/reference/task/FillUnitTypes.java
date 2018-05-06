package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.UnitTypeDAO;
import reference.init.ModuleConst;
import reference.model.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//run task ref_fill_unit_types
@Command(name = ModuleConst.CODE + "_fill_unit_types")
public class FillUnitTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<UnitType> entities = new ArrayList<UnitType>();

        String[] data = {"person", "thousand_persons", "square_meter", "tenge", "thousand_tenge", "million_tenge", "billion_tenge", "trillion_tenge", "unit", "thousand_units", "incident"};
        String[] dataEng = {"person", "thousand persons", "square meter", "tenge", "thousand tenge", "mill.tenge", "bill.tenge", "trillion tenge", "unit", "thousand units", "incident"};
        String[] dataRus = {"человек", "тысяча человек", "кв.метр", "тенге", "тыс.тенге", "млн.тенге", "млрд.тенге", "триллион тенге", "единиц", "тыс.единиц", "случай"};
        String[] dataKaz = {"адам", "мың адам", "шаршы метр", "теңге", "мың теңге", "млн.теңге", "млрд.теңге", "триллион теңге", "бірлік", "мың бірлік", "оқиға"};
        String[] categories = {"people", "people", "area", "money_kz", "money_kz", "money_kz", "money_kz", "money_kz", "quantity", "quantity", "event"};

        long[] factor = {1, 1000, 1, 1, 1000, 1000000, 1000000000, 1000000000000L, 1, 1000, 1};

        for (int i = 0; i < data.length; i++) {
            UnitType entity = new UnitType();
            entity.setName(data[i]);
            entity.setCategory(categories[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            entity.setLocName(name);
            entity.setFactor(factor[i]);
            entities.add(entity);
        }

        try {
            UnitTypeDAO dao = new UnitTypeDAO(ses);
            for (UnitType entry : entities) {
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
