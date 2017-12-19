package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.RoadRepairTypeDAO;
import reference.init.ModuleConst;
import reference.model.RoadRepairType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_road_repair_types")
public class FillRoadRepairTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<RoadRepairType> entities = new ArrayList<>();

        String names[] = {"current", "medium", "capital", "reconstruction"};
        String namesEng[] = {"Current", "Medium", "Capital", "Reconstruction"};
        String namesRus[] = {"Текущий", "Средний", "Капитальный", "Реконструция"};
        String namesKaz[] = {"Ағымдағы", "Орташа", "Капитал", "Қайта құру"};

        for (int i = 0; i < names.length; i++) {
            RoadRepairType entity = new RoadRepairType();
            entity.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            RoadRepairTypeDAO dao = new RoadRepairTypeDAO(ses);
            for (RoadRepairType entry : entities) {
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
