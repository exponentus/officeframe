package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.LocalityDAO;
import reference.dao.LocalityTypeDAO;
import reference.dao.RegionDAO;
import reference.model.Locality;
import reference.model.Region;
import reference.model.constants.LocalityCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "fill_localities")
public class FillLocalities extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<Locality> entities = new ArrayList<>();
        String[] data = {"Kapchagay", "Taldykorgan"};
        String[] data1 = {"Almaty"};
        String[] data2 = {"Pavloadar", "Aksu", "Ekibastuz"};
        String[] data2Rus = {"Павлодар", "Аксу", "Экибастуз"};

        String[] zhambylData = {"taraz", "karatau", "zhanatas"};
        String[] zhambylRegionLocalitiesEng = {"Taraz", "Karatau", "Zhanatas"};
        String[] zhambylRegionLocalitiesRus = {"Тараз", "Каратау", "Жанатас"};
        String[] zhambylRegionLocalitiesKaz = {"Тараз", "Каратау", "Жанатас"};

        Region d = null;
        LocalityTypeDAO ltDao = null;
        RegionDAO cDao = null;
        try {
            ltDao = new LocalityTypeDAO(ses);
            cDao = new RegionDAO(ses);
            d = cDao.findByName("almaty_region");

            if (d != null) {
                for (String val : data) {
                    Locality entity = new Locality();
                    entity.setRegion(d);
                    entity.setName(val);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    entities.add(entity);
                }
            }

            d = cDao.findByName("almaty");

            if (d != null) {
                for (String val : data1) {
                    Locality entity = new Locality();
                    entity.setRegion(d);
                    entity.setName(val);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    entities.add(entity);
                }
            }

            d = cDao.findByName("pavloadar_region");

            if (d != null) {
                for (int i = 0; i < data2.length; i++) {
                    Locality entity = new Locality();
                    entity.setRegion(d);
                    entity.setName(data2[i]);
                    Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
                    name.put(LanguageCode.ENG, data2[i]);
                    name.put(LanguageCode.RUS, data2Rus[i]);
                    name.put(LanguageCode.KAZ, data2Rus[i]);
                    entity.setLocName(name);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    entities.add(entity);
                }
            }

            d = cDao.findByName("zhambyl_region");

            if (d != null) {
                for (int i = 0; i < zhambylData.length; i++) {
                    Locality entity = new Locality();
                    entity.setRegion(d);
                    entity.setName(zhambylData[i]);
                    Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
                    name.put(LanguageCode.ENG, zhambylRegionLocalitiesEng[i]);
                    name.put(LanguageCode.RUS, zhambylRegionLocalitiesRus[i]);
                    name.put(LanguageCode.KAZ, zhambylRegionLocalitiesKaz[i]);
                    entity.setLocName(name);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    entities.add(entity);
                }
            }

        } catch (DAOException e) {
            e.printStackTrace();
        }

        try {
            LocalityDAO dao = new LocalityDAO(ses);
            for (Locality entry : entities) {
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
