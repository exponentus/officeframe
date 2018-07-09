package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.StatisticIndicatorTypeDAO;
import reference.dao.StatisticTypeDAO;
import reference.dao.UnitTypeDAO;
import reference.init.ModuleConst;
import reference.model.StatisticIndicatorType;
import reference.model.StatisticType;
import reference.model.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//run task ref_fill_statistic_indicator_types
@Command(name = ModuleConst.CODE + "_fill_statistic_indicator_types")
public class FillStatisticIndicatorTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<StatisticIndicatorType> entities = new ArrayList<>();

        StatisticTypeDAO statisticTypeDAO = null;
        UnitTypeDAO unitTypeDAO = null;
        try {
            statisticTypeDAO = new StatisticTypeDAO(ses);
            unitTypeDAO = new UnitTypeDAO(ses);
        } catch (DAOException e) {
            e.printStackTrace();
        }
        List<StatisticType> list = statisticTypeDAO.findAll();
        for (StatisticType st : list) {
            StatisticIndicatorType statisticIndicatorType = new StatisticIndicatorType();


            if(!st.getName().equalsIgnoreCase("entrepreneurship") ) {
                UnitType unitType = null;
                try {
                    unitType = unitTypeDAO.findByName("tenge");
                } catch (DAOException e) {
                    e.printStackTrace();
                }
                statisticIndicatorType.setName("total_turnover_" + st.getName());
                Map<LanguageCode, String> name = new HashMap<>();
                name.put(LanguageCode.RUS, "Общий оборот в денежном выражении");
                name.put(LanguageCode.ENG, "Total turnover in monetary terms");
                name.put(LanguageCode.KAZ, "Ақшалай айналымдағы жалпы айналым");
                statisticIndicatorType.setLocName(name);
                statisticIndicatorType.setStatisticType(st);
                statisticIndicatorType.setUnitType(unitType);
                entities.add(statisticIndicatorType);
            }else{
                UnitType unitType = null;
                try {
                    unitType = unitTypeDAO.findByName("unit");
                } catch (DAOException e) {
                    e.printStackTrace();
                }
                String _names[] = {"in_count_entrepreneurship", "by_number_entrepreneurship", "number_of_enterprises_entrepreneurship"};
                String _namesEng[] = {"In count", "By number", "Number of enterprises"};
                String _namesRus[] = {"По количеству", "По численности", "Количество предприятий"};
                String _namesKaz[] = {"сан бойынша", "сан бойынша", "Кәсіпорындар саны"};
                for (int k = 0; k < _names.length; k++) {
                    StatisticIndicatorType _statisticIndicatorType = new StatisticIndicatorType();
                    _statisticIndicatorType.setName(_names[k]);
                    Map<LanguageCode, String> name = new HashMap<>();
                    name.put(LanguageCode.RUS, _namesRus[k]);
                    name.put(LanguageCode.ENG, _namesEng[k]);
                    name.put(LanguageCode.KAZ, _namesKaz[k]);
                    _statisticIndicatorType.setLocName(name);
                    _statisticIndicatorType.setStatisticType(st);
                    _statisticIndicatorType.setUnitType(unitType);
                    entities.add(_statisticIndicatorType);

                }
            }
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
