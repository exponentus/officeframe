package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.StatisticIndicatorTypeDAO;
import reference.dao.StatisticTypeDAO;
import reference.init.ModuleConst;
import reference.model.StatisticIndicatorType;
import reference.model.StatisticType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_statistic_indicator_types")
public class FillStatisticIndicatorTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<StatisticIndicatorType> entities = new ArrayList<>();

        String names[] = {"", "", ""};
        String namesEng[] = {"", "", ""};
        String namesRus[] = {"", "", ""};
        String namesKaz[] = {"", "", ""};

        for (int i = 0; i < names.length; i++) {
            StatisticIndicatorType statisticIndicatorType = new StatisticIndicatorType();
            statisticIndicatorType.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            statisticIndicatorType.setLocName(name);
            entities.add(statisticIndicatorType);
        }

        try {
           StatisticIndicatorTypeDAO dao = new StatisticIndicatorTypeDAO(ses);
            for (StatisticIndicatorType entry : entities) {
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
