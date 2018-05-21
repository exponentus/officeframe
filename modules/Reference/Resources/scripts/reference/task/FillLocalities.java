package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.DistrictDAO;
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

//run task fill_localities
@Command(name = "fill_localities")
public class FillLocalities extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<Locality> entities = new ArrayList<>();
        String[] almatyData = {"Kapchagay", "Taldykorgan"};
        String[] almatyDataEng = {"Almaty"};

        String[] pavlodarData = {"pavloadar", "aksu", "ekibastuz"};
        String[] pavlodarDataEng = {"Pavloadar", "Aksu", "Ekibastuz"};
        String[] pavlodarDataRus = {"Павлодар", "Аксу", "Экибастуз"};
        String[] pavlodarDataKaz = {"Павлодар", "Ақсу", "Екібастұз"};

        String[] zhambylData = {"taraz", "karatau", "zhanatas"};
        String[] zhambylDataEng = {"Taraz", "Karatau", "Zhanatas"};
        String[] zhambylDataRus = {"Тараз", "Каратау", "Жанатас"};
        String[] zhambylDataKaz = {"Тараз", "Каратау", "Жанатас"};

        Region d = null;
        LocalityTypeDAO ltDao = null;
        RegionDAO cDao = null;
        try {
            ltDao = new LocalityTypeDAO(ses);
            cDao = new RegionDAO(ses);
            DistrictDAO districtDAO = new DistrictDAO(ses);
            d = cDao.findByName("almaty_region");

            if (d != null) {
                for (String val : almatyData) {
                    Locality entity = new Locality();
                    entity.setRegion(d);
                    entity.setName(val);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    entities.add(entity);
                }
            }

            d = cDao.findByName("almaty");

            if (d != null) {
                for (String val : almatyDataEng) {
                    Locality entity = new Locality();
                    entity.setRegion(d);
                    entity.setName(val);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    entities.add(entity);
                }
            }

            d = cDao.findByName("pavlodar_region");

            if (d != null) {
                for (int i = 0; i < pavlodarData.length; i++) {
                    Locality entity = new Locality();
                    entity.setRegion(d);
                    entity.setName(pavlodarData[i]);
                    Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
                    name.put(LanguageCode.ENG, pavlodarDataEng[i]);
                    name.put(LanguageCode.RUS, pavlodarDataRus[i]);
                    name.put(LanguageCode.KAZ, pavlodarDataKaz[i]);
                    entity.setLocName(name);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    if (entity.getName().equals("pavlodar")) {
                        entity.setDistrict(districtDAO.findByName("pavlodar"));
                    } else if (entity.getName().equals("aksu")) {
                        entity.setDistrict(districtDAO.findByName("aksu"));
                    } else if(entity.getName().equals("ekibastuz")){
                        entity.setDistrict(districtDAO.findByName("ekibastuz"));
                    }
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
                    name.put(LanguageCode.ENG, zhambylDataEng[i]);
                    name.put(LanguageCode.RUS, zhambylDataRus[i]);
                    name.put(LanguageCode.KAZ, zhambylDataKaz[i]);
                    entity.setLocName(name);
                    entity.setType(ltDao.findByCode(LocalityCode.CITY));
                    if (entity.getName().equals("karatau")) {
                        entity.setDistrict(districtDAO.findByName("talasky"));
                    } else if (entity.getName().equals("zhanatas")) {
                        entity.setDistrict(districtDAO.findByName("sarysusky"));
                    }
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
