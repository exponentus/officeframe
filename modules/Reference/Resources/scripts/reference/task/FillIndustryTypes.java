package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.ActivityTypeCategoryDAO;
import reference.dao.IndustryTypeDAO;
import reference.init.ModuleConst;
import reference.model.ActivityTypeCategory;
import reference.model.IndustryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//run task ref_fill_industry_types
@Command(name = ModuleConst.CODE + "_fill_industry_types")
public class FillIndustryTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<IndustryType> entities = new ArrayList<IndustryType>();
        String[] data = {"mining_and_quarrying", "manufacturing_industry", "electricity",
                "gas_steam_and_air_conditioning", "water_supply", "sewerage_system",
                "control_over_the_collection_and_distribution_of_waste"};

        String[] dataRus = {"горнодобывающая промышленность и разработка карьеров", "обрабатывающая промышленность",
                "электроснабжение", "подача газа, пара и воздушное кондиционирование",
                "водоснабжение", "канализационная система", "контроль над сбором и распределением отходов"};

        try {

            ActivityTypeCategory cat = new ActivityTypeCategoryDAO(ses).findByName(ModuleConst.ACTIVITY_TYPE_CATEGORY_FOR_INDUSTRY);
            if (cat != null) {
                for (int i = 0; i < data.length; i++) {
                    IndustryType entity = new IndustryType();
                    entity.setName(data[i]);
                    Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
                    name.put(LanguageCode.ENG, data[i]);
                    name.put(LanguageCode.RUS, dataRus[i]);
                    entity.setLocName(name);
                    entity.setCategory(cat);
                    entities.add(entity);
                }

                IndustryTypeDAO dao = new IndustryTypeDAO(ses);
                for (IndustryType entry : entities) {
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
            } else {
                logger.error("Category \"" + ModuleConst.ACTIVITY_TYPE_CATEGORY_FOR_INDUSTRY + "\" has not been found");
            }
        } catch (DAOException e) {
            logger.exception(e);
        }
        logger.info("done...");
    }

}
