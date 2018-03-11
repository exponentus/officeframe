package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.util.StringUtil;
import reference.dao.DistrictDAO;
import reference.dao.RegionDAO;
import reference.init.ModuleConst;
import reference.model.District;
import reference.model.Region;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_districts")
public class FillDistricts extends Do {
    private static final String KMZ_FILE = "kz.kmz";

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<District> entities = new ArrayList<>();
        String[] almatyDistricts = {"karasay", "talgar"};
        String[] pavlodarDistricts = {"aktogayskiy", "bayanaulskiy", "zhelezinskiy", "irtyshskiy", "kachirskiy",
                "lebyazhinskiy", "mayskiy", "pavlodarskiy", "uspenskiy", "sherbaktinskiy"};
        String[] pavlodarDistrictsEng = {"Aktogayskiy", "Bayanaulskiy", "Zhelezinskiy", "Irtyshskiy", "Kachirskiy", "Lebyazhinskiy", "Mayskiy",
                "Pavlodarskiy", "Uspenskiy", "Sherbaktinskiy"};
        String[] pavlodarDistrictsRus = {"Актогайский", "Баянаульский", "Железинский", "Иртышский", "Качирский", "Лебяжинский", "Майский",
                "Павлодарский", "Успенский", "Щербактинский"};
        String[] pavlodarDistrictsKaz = {"Актогайский", "Баянаульский", "Железинский", "Иртышский", "Качирский", "Лебяжинский", "Майский",
                "Павлодарский", "Успенский", "Щербактинский"};

        String[] zhambylDistricts = {"talasky", "shusky", "zhambulsky", "kordaysky", "zhualynsky", "sarysusky", "ryskulovsky",
                "moinkumsky", "baizaksky", "merkensky"};
        String[] zhambylDistrictsEng = {"Talasky", "Shusky", "Zhambulsky", "Kordaysky", "Zhualynsky", "Sarysusky", "Ryskulovsky",
                "Moinkumsky", "Baizaksky", "Merkensky"};
        String[] zhambylDistrictsRus = {"Талаский", "Шуский", "Жамбылский", "Кордайский", "Жуалынский", "Сарысуский", "Т.Рыскуловский",
                "Моинкумский", "Байзакский", "Меркенский"};
        String[] zhambylDistrictsKaz = {"Талаский", "Шуский", "Жамбылский", "Кордайский", "Жуалынский", "Сарысуский", "Т.Рыскуловский",
                "Моинкумский", "Байзакский", "Меркі"};

        Region region = null;
        RegionDAO cDao = null;
        try {
            cDao = new RegionDAO(ses);

            region = cDao.findByName("almaty_region");
            for (int i = 0; i < almatyDistricts.length; i++) {
                District entity = new District();
                entity.setRegion(region);
                entity.setName(almatyDistricts[i]);
                entities.add(entity);
            }

            region = cDao.findByName("pavlodar_region");
            if (region != null) {
                for (int i = 0; i < pavlodarDistricts.length; i++) {
                    entities.add(composeDistrict(i, region, pavlodarDistricts, pavlodarDistrictsEng, pavlodarDistrictsRus, pavlodarDistrictsKaz));
                }
            }

            region = cDao.findByName("zhambyl_region");
            if (region != null) {
                for (int i = 0; i < zhambylDistricts.length; i++) {
                    entities.add(composeDistrict(i, region, zhambylDistricts, zhambylDistrictsEng, zhambylDistrictsRus, zhambylDistrictsKaz));

                }
            }


        } catch (DAOException e) {
            e.printStackTrace();
        }

        try {
            DistrictDAO dao = new DistrictDAO(ses);
            for (District entry : entities) {
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

    private District composeDistrict(int i, Region region, String[] names, String[] namesEng, String[] namesRus, String[] namesKaz) {
        District entity = new District();
        entity.setRegion(region);
        entity.setName(names[i]);
        Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
        name.put(LanguageCode.ENG, namesEng[i]);
        name.put(LanguageCode.RUS, namesRus[i]);
        name.put(LanguageCode.KAZ, namesKaz[i]);
        entity.setLocName(name);
        entity.setLatLng(StringUtil.convertKMLToJSArray(names[i],
                EnvConst.RESOURCES_DIR + File.separator + KMZ_FILE));
        return entity;
    }
}
