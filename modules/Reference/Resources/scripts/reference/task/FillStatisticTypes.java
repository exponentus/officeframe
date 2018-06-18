package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.BuildingStateDAO;
import reference.dao.StatisticTypeDAO;
import reference.init.ModuleConst;
import reference.model.BuildingState;
import reference.model.StatisticType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_statistic_types")
public class FillStatisticTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<StatisticType> entities = new ArrayList<>();

        String names[] = {"vrp", "industry", "agriculture", "investment", "construction", "building_commissioning", "entrepreneurship", "retail_turnover", "wholesale_turnover", "inflation", "manpower", "crime"};
        String namesEng[] = {"VRP", "Industry", "Agriculture","Investment","Construction","Building commissioning","Entrepreneurship","Retail turnover","Wholesale turnover","Inflation","Manpower","Crime"};
        String namesRus[] = {"Валовый региональный продукт", "Промышленность", "Сельское хозяйство", "Инвестиции", "Строительство", "Ввод в эксплуатацию жилых здании", "Предпринимательство", "Розничный товарооборот", "Оптовый товарооборот","Инфляция","Рабочая сила","Преступность"};
        String namesKaz[] = {"", "", "","","","","","","","","",""};// TODO add kazakh translate

        for (int i = 0; i < names.length; i++) {
            StatisticType statisticType = new StatisticType();
            statisticType.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            statisticType.setLocName(name);
            entities.add(statisticType);
        }

        try {
           StatisticTypeDAO dao = new StatisticTypeDAO(ses);
            for (StatisticType entry : entities) {
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
