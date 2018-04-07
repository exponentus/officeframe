package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.util.StringUtil;
import reference.dao.CityDistrictDAO;
import reference.dao.LocalityDAO;
import reference.model.CityDistrict;
import reference.model.Locality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "fill_city_districts")
public class FillCityDistricts extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {

        List<CityDistrict> entities = new ArrayList<>();
        String[] data = {"Алатауский", "Алмалинский", "Ауэзовский", "Бостандыкский", "Жетысуский", "Медеуский",

                "Наурызбайский", "Турксибский"};
        Locality region = null;
        try {
            LocalityDAO cDao = new LocalityDAO(ses);
            region = cDao.findByName("almaty");
            if (region != null) {
                for (int i = 0; i < data.length; i++) {
                    CityDistrict entity = new CityDistrict();
                    entity.setLocality(region);
                    String latName = StringUtil.convertRusToLat(data[i]);
                    entity.setName(latName.replaceAll(" ", "p"));
                    Map<LanguageCode, String> localizedNames = new HashMap<>();
                    localizedNames.put(LanguageCode.ENG, latName);
                    localizedNames.put(LanguageCode.RUS, data[i]);
                    localizedNames.put(LanguageCode.KAZ, data[i]);
                    entity.setLocName(localizedNames);
                    entities.add(entity);
                }
                try {
                    CityDistrictDAO dao = new CityDistrictDAO(ses);
                    for (CityDistrict entry : entities) {
                        try {
                            if (dao.add(entry) != null) {
                                logger.info(entry.getName() + " added");
                            }
                        } catch (DAOException e) {
                            if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                                logger.warning(
                                        "a data is already exists (" + e.getAddInfo() + "), record was skipped");
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
            } else {
                logger.error("Locality has not been found");
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        logger.info("done...");
    }

}
