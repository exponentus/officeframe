package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.LandClassificationDAO;
import reference.dao.PropertyCodeDAO;
import reference.init.AppConst;
import reference.model.LandClassification;
import reference.model.PropertyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Command(name = AppConst.CODE + "_fill_land_classifications")
public class FillLandClassifications extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<LandClassification> entities = new ArrayList<LandClassification>();
        String[] data = {"agricultural_land", "land_of_settlements", "special_purpose_lands",
                "lands_of_specially_protected_territories_and_objects", "land_of_forest_fund", "land_of_water_fund", "land_of_reserve"};
        String[] dataKaz = {"Ауылшаруашылық жері", "Елді мекендер", "Арнайы мақсаттағы жер",
                "Ерекше қорғалатын аумақтар мен объектілердің жерлері", "Орман қорының жері", "Су қорының жері", "Қорық жері"};
        String[] dataEng = {"Agricultural Land", "Land of Settlements", "Special Purpose Lands",
                "Lands of Specially Protected Territories and Objects", "Land of Forest Fund", "Land of Water Fund", "Land of Reserve"};
        String[] dataRus = {"Земли сельскохозяйственного назначения", "Земли населенных пунктов", "Земли специального назначения",
                "Земли особо охраняемых территории и объектов", "Земли лесного фонда", "Земли водного фонда", "Земли запаса"};

        for (int i = 0; i < data.length; i++) {
            LandClassification entity = new LandClassification();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.KAZ, dataKaz[i]);
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            LandClassificationDAO dao = new LandClassificationDAO(ses);
            for (LandClassification entry : entities) {
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
