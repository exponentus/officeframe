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
import reference.dao.RegionDAO;
import reference.init.ModuleConst;
import reference.model.District;
import reference.model.Region;

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

        String[] pavlodarDistricts = {"pavlodar", "aksu", "ekibastuz", "aktogayskiy", "bayanaulskiy", "zhelezinskiy", "irtyshskiy", "kachirskiy",
                "lebyazhinskiy", "mayskiy", "pavlodarskiy", "uspenskiy", "sherbaktinskiy"};
        String[] pavlodarDistrictsEng = {"Pavlodar", "Aksu", "Ekibastuz", "Aktogayskiy", "Bayanaulskiy", "Zhelezinskiy", "Irtyshskiy", "Kachirskiy", "Lebyazhinskiy", "Mayskiy",
                "Pavlodarskiy", "Uspenskiy", "Sherbaktinskiy"};
        String[] pavlodarDistrictsRus = {"Павлодар", "Аксу", "Экибастуз", "Актогайский", "Баянаульский", "Железинский", "Иртышский", "Качирский", "Лебяжинский", "Майский",
                "Павлодарский", "Успенский", "Щербактинский"};
        String[] pavlodarDistrictsKaz = {"Павлодар", "Ақсу", "Екібастұз", "Актогайский", "Баянаульский", "Железинский", "Иртышский", "Качирский", "Лебяжинский", "Майский",
                "Павлодарский", "Успенский", "Щербактинский"};
        String[] pavlodarCoords = {
                "52.3165549, 76.8947629",
                "52.0811978, 76.8323266",
                "51.726116, 75.2844142",
                "52.6787855, 74.5943461",
                "50.8145591, 74.7426571",
                "53.8408406, 75.5151388",
                "53.3322423, 74.0182521",
                "53.2939465, 75.9202601",
                "51.5023614, 77.8370267",
                "50.921981, 76.3697979",
                "52.3396825, 76.6432955",
                "53.1210563, 76.8922063",
                "52.3257041, 77.7659608"
        };

        String[] zhambylDistricts = {"talasky", "shusky", "zhambulsky", "kordaysky", "zhualynsky", "sarysusky", "ryskulovsky",
                "moinkumsky", "baizaksky", "merkensky"};
        String[] zhambylDistrictsEng = {"Talasky", "Shusky", "Zhambulsky", "Kordaysky", "Zhualynsky", "Sarysusky", "Ryskulovsky",
                "Moinkumsky", "Baizaksky", "Merkensky"};
        String[] zhambylDistrictsRus = {"Талаский", "Шуский", "Жамбылский", "Кордайский", "Жуалынский", "Сарысуский", "Т.Рыскуловский",
                "Моинкумский", "Байзакский", "Меркенский"};
        String[] zhambylDistrictsKaz = {"Талаский", "Шуский", "Жамбылский", "Кордайский", "Жуалынский", "Сарысуский", "Т.Рыскуловский",
                "Моинкумский", "Байзакский", "Меркі"};
        String[] zhambylCoords = {
                "43.6778202, 70.1352499",
                "43.8985069, 73.3679775",
                "43.044914, 70.9009305",
                "43.5467407, 74.4027515",
                "42.7444656, 70.347537",
                "44.607045, 69.0893988",
                "43.2412534, 71.8772686",
                "44.9082663, 71.4718903",
                "43.4039937, 71.0635941",
                "43.1979484, 72.5254618"
        };

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
                    entities.add(composeDistrict(i, region, pavlodarDistricts, pavlodarDistrictsEng, pavlodarDistrictsRus, pavlodarDistrictsKaz, pavlodarCoords));
                }
            }

            region = cDao.findByName("zhambyl_region");
            if (region != null) {
                for (int i = 0; i < zhambylDistricts.length; i++) {
                    entities.add(composeDistrict(i, region, zhambylDistricts, zhambylDistrictsEng, zhambylDistrictsRus, zhambylDistrictsKaz, zhambylCoords));

                }
            }


        } catch (DAOException e) {
            e.printStackTrace();
        }

        try {
            DistrictDAO dao = new DistrictDAO(ses);
            for (District entry : entities) {
                try {
                    District existEntry = dao.findByName(entry.getName());
                    if (existEntry != null) {
                        existEntry.setRegion(entry.getRegion());
                        existEntry.setLocName(entry.getLocName());
                        existEntry.setLatLng(entry.getLatLng());
                        existEntry.setTitle(entry.getTitle());
                        if (dao.update(existEntry) != null) {
                            logger.info(entry.getName() + " updated");
                        }
                    } else {
                        if (dao.add(entry) != null) {
                            logger.info(entry.getName() + " added");
                        }
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

    private District composeDistrict(int i, Region region, String[] names, String[] namesEng, String[] namesRus, String[] namesKaz, String[] coord) {
        District entity = new District();
        entity.setRegion(region);
        entity.setName(names[i]);
        entity.setTitle(namesRus[i]);
        Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
        name.put(LanguageCode.ENG, namesEng[i]);
        name.put(LanguageCode.RUS, namesRus[i]);
        name.put(LanguageCode.KAZ, namesKaz[i]);
        entity.setLocName(name);
        entity.setLatLng(coord[i]);
//        entity.setLatLng(StringUtil.convertKMLToJSArray(names[i],
//                EnvConst.RESOURCES_DIR + File.separator + KMZ_FILE));
        return entity;
    }
}
