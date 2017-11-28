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
import reference.init.AppConst;
import reference.model.UnitType;
import reference.model.constants.UnitCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//run task ref_fill_unit_types
@Command(name = AppConst.CODE + "_fill_unit_types")
public class FillUnitTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<UnitType> entities = new ArrayList<UnitType>();
        String[] data = {"tenge", "million_tenge", "billion_tenge"};
        String[] dataEng = {"tenge", "mill.tenge","bill.tenge"};
        String[] dataRus = {"тенге", "млн.тенге","млрд.тенге"};
        String[] dataKaz = {"теңге", "млн.теңге","млрд.теңге"};


        for (int i = 0; i < data.length; i++) {
            UnitType entity = new UnitType();
            entity.setName(data[i]);
            entity.setCategory(UnitCategory.MONEY_KZ);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            entity.setLocName(name);
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
