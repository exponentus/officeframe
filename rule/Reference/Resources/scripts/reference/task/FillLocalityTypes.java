package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.LocalityTypeDAO;
import reference.model.LocalityType;
import reference.model.constants.LocalityCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "fill_locality_types")
public class FillLocalityTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<LocalityType> entities = new ArrayList<LocalityType>();
        String[] dataEng = {"City", "Village"};
        String[] data = {"Город", "Село"};
        String[] dataKZ = {"Город", "Село"};
        LocalityCode[] code = {LocalityCode.CITY, LocalityCode.VILLAGE};

        for (int i = 0; i < data.length; i++) {
            LocalityType entity = new LocalityType();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.KAZ, dataKZ[i]);
            name.put(LanguageCode.RUS, data[i]);
            entity.setLocName(name);
            entity.setCode(code[i]);
            entities.add(entity);
        }

        try {
            LocalityTypeDAO dao = new LocalityTypeDAO(ses);
            for (LocalityType entry : entities) {
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
