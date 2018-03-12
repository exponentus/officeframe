package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.common.task.AbstractGen;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.AsOfDAO;
import reference.init.ModuleConst;
import reference.model.AsOf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//run task ref_gen_asof_test_data
@Command(name = ModuleConst.CODE + "_gen_asof_test_data")
public class GenerateAsOfData extends AbstractGen {


    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        String data[] = {"by_begin_of_2017_year", "by_begin_of_2016_year", "by_begin_of_1_quarter_of_2017_year"};
        String dataEng[] = {"by begin of 2017 year", "by begin of 2016 year", "by begin of 1 quarter of 2017 year"};
        String dataRus[] = {"к началу 2017 года", "к началу 2016 года", "к началу 1 квартала 2017 года"};
        String dataKaz[] = {"2017 жылдың басында", "2016 жылдың басында", "2017 жылдың бірінші тоқсанының басында"};

        List<AsOf> entities = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            AsOf entity = new AsOf();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            entity.setLocName(name);
            //     entity.setSchema(DisplayingSchema.SHOW_FOR_ALL);
            entities.add(entity);
        }

        try {
            AsOfDAO dao = new AsOfDAO(ses);
            for (AsOf entry : entities) {
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
