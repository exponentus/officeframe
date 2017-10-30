package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.MemorialTypeDAO;
import reference.init.AppConst;
import reference.model.MemorialType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_memorial_types")
public class FillMemorialTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<MemorialType> entities = new ArrayList<MemorialType>();
        String[] data = {"building", "construction", "residential", "land"};
        String[] dataEng = {"Building", "Construction", "Residential", "Land"};
        String[] dataRus = {"Здание", "Строение", "Жилой объект", "Земля",};
        String[] dataKaz = {"Ғимарат", "Құрылыс", "Тұрғын үй", "Жер"};

        try {

            for (int i = 0; i < data.length; i++) {
                MemorialType entity = new MemorialType();
                entity.setName(data[i]);
                Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
                name.put(LanguageCode.ENG, dataEng[i]);
                name.put(LanguageCode.RUS, dataRus[i]);
                name.put(LanguageCode.KAZ, dataKaz[i]);
                entity.setLocName(name);
                entities.add(entity);
            }

            MemorialTypeDAO dao = new MemorialTypeDAO(ses);
            for (MemorialType entry : entities) {
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
