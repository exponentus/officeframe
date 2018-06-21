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

@Command(name = ModuleConst.CODE + "_fill_statistic_indicator_types")
public class FillStatisticIndicatorTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<StatisticIndicatorType> entities = new ArrayList<>();
        String names[] = {"taxes_on_products", "provision_other_types_services", "arts_entertainment_recreation","health_social_services","education", "public_administration_defense", "compulsory_social_security", "activities_in_field_administrative_support_services", "professional_scientific_technical_activities", "operations_with_real_estate", "financial_and_insurance_activities", "information_and_communication", "accommodation_food_services","transport_and_storage","wholesale_retail_trade","repair_cars_motorcycles","building","industry","agriculture_forestry_fisheries"};
        String namesEng[] = {"Taxes on products", "Provision of other types of services", "Arts, entertainment and recreation","Health and social services","Education", "Public administration and defense", "Compulsory social security", "Activities in the field of administrative and support services", "Professional, scientific and technical activities", "Operations with real estate", "Financial and insurance activities", "Information and communication", "Accommodation and food services","Transport and storage","Wholesale and retail trade","Repair of cars and motorcycles","Building","Industry","Agriculture, forestry and fisheries"};
        String namesRus[] = {"Налоги на продукты", "Предоставление прочих видов услуг", "Искусство, развлечения и отдых", "Здравоохранение и социальные услуги", "Образование", "Государственное управление и оборона", "Обязательное социальное обеспечение","Деятельность в области административного и вспомогательного обслуживания","Профессиональная, научная и техническая деятельность","Операции с недвижимым имуществом", "Финансовая и страховая деятельность","Информация и связь", "Услуги по проживанию и питанию","Транспорт и складирование","Оптовая и розничная торговля","Ремонт автомобилей и мотоциклов","Строительство","Промышленность","Сельское, лесное и рыбное хозяйство"};
        String namesKaz[] = {"", "", "","","","","","","","","","","","","","","","",""};// TODO add kazakh translate

        /*for (int i = 0; i < names.length; i++) {
            StatisticIndicatorType statisticIndicatorType = new StatisticIndicatorType();
            statisticIndicatorType.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            statisticIndicatorType.setLocName(name);
            entities.add(statisticIndicatorType);
        }*/
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