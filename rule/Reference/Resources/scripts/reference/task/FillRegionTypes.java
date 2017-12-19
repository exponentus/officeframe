package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.RegionTypeDAO;
import reference.init.ModuleConst;
import reference.model.RegionType;
import reference.model.constants.RegionCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_region_types")
public class FillRegionTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<RegionType> entities = new ArrayList<>();

        String[] data = {"federation", "region", "urban_agglomeration"};
        String[] dataEng = {"Federation", "Region", "Urban agglomeration"};
        String[] dataRus = {"Федерация", "Область", "Город"};
        String[] dataKaz = {"Федерация", "Аймақ", "Қалалық агломерация"};
        RegionCode[] code = {RegionCode.FEDERATION, RegionCode.REGION, RegionCode.URBAN_AGGLOMERATION};

        for (int i = 0; i < data.length; i++) {
            RegionType entity = new RegionType();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            entity.setLocName(name);
            entity.setCode(code[i]);
            entities.add(entity);
        }

        try {
            RegionTypeDAO dao = new RegionTypeDAO(ses);
            for (RegionType entry : entities) {
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
