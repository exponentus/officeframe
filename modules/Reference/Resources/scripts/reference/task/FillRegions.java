package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.CountryDAO;
import reference.dao.RegionDAO;
import reference.dao.RegionTypeDAO;
import reference.model.Country;
import reference.model.Region;
import reference.model.RegionType;
import reference.model.constants.RegionCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "fill_regions")
public class FillRegions extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<Region> entities = new ArrayList<>();

        String[] data = {"akmola_region", "aktobe_region", "almaty", "astana", "almaty_region", "pavlodar_region", "zhambyl_region",
                "atyray_region", "zko_region", "karagandy_region", "kostanay_region", "kyzyl-orda_region", "mangystay_region", "uko_region"
                , "sko_region", "vko_region"};
        String[] namesEng = {"Akmola region", "Aktobe region", "Almaty", "Astana", "Almaty region", "Pavlodar region", "Zhambyl region",
                "Atyray region", "ZKO region", "Karagandy region", "Kostanay region", "Kyzyl-orda region", "Mangystay region", "UKO region",
                "SKO region", "VKO region"};
        String[] namesRus = {"Акмолинская область", "Актюбинская область", "Алматы", "Астана", "Алматинская область", "Павлодарская область", "Жамбылская область", "Атырауская область", "Западно-Казахстанская область", "Карагандинская область", "Костанайская область", "Кызыл-Ординская область", "Мангистауская область", "Южно-Казахстанская область", "Северно-Казахстанская область", "Восточно-Казахстанская область"};
        String[] namesKaz = {"Ақмола облысы", "Ақтөбе облысы", "Алматы", "Астана", "Алматы облысы", "Павлодар облысы", "Жамбыл облысы", "Атырау облысы", "Батыс Қазақстан облысы", "Қарағанды облысы", "Қостанай облысы", "Қызылорда облысы", "Маңғыстау облысы", "Оңтүстік Қазақстан облысы", "Солтүстік Қазақстан облысы","Шығыс Қазақстан облысы"};
        try {
            CountryDAO cDao = new CountryDAO(ses);
            Country country = null;

            country = cDao.findByName("kazakhstan");

            for (int i = 0; i < data.length; i++) {
                Region entity = new Region();
                entity.setCountry(country);
                entity.setTitle(namesRus[i]);
                entity.setName(data[i]);
                Map<LanguageCode, String> name = new HashMap<>();
                name.put(LanguageCode.RUS, namesRus[i]);
                name.put(LanguageCode.ENG, namesEng[i]);
                name.put(LanguageCode.KAZ, namesKaz[i]);
                entity.setLocName(name);
                RegionTypeDAO rtDao = new RegionTypeDAO(ses);
                RegionType rType = null;
                if (data[i].equals("almaty") || data[i].equals("astana")) {
                    rType = rtDao.findByCode(RegionCode.URBAN_AGGLOMERATION);
                } else {
                    rType = rtDao.findByCode(RegionCode.REGION);
                }
                entity.setType(rType);
                entities.add(entity);
            }

            String[] data1 = {"moscow", "saint_petersburg"};
            String[] names1Eng = {"Moscow", "Saint Petersburg"};
            String[] names1Rus = {"Москва", "Санкт-Петербург"};
            String[] names1Kaz = {"Мәскеу", "Санкт-Петербург"};
            Country country1 = null;
            country1 = cDao.findByName("russia");

            for (int i = 0; i < data1.length; i++) {
                Region entity = new Region();
                entity.setCountry(country1);
                entity.setName(data1[i]);
                Map<LanguageCode, String> name = new HashMap<>();
                name.put(LanguageCode.RUS, names1Rus[i]);
                name.put(LanguageCode.ENG, names1Eng[i]);
                name.put(LanguageCode.KAZ, names1Kaz[i]);
                entity.setLocName(name);
                RegionTypeDAO rtDao = new RegionTypeDAO(ses);
                RegionType rType = null;
                if (data1[i].equals("moscow") || data1[i].equals("saint_petersburg")) {
                    rType = rtDao.findByCode(RegionCode.URBAN_AGGLOMERATION);
                } else {
                    rType = rtDao.findByCode(RegionCode.REGION);
                }
                entity.setType(rType);
                entities.add(entity);
            }

            String[] data2 = {"lisbon", "leiria"};
            String[] names2Eng = {"Lisbon", "Leiria"};
            String[] names2Rus = {"Лиссабон", "Лейрия"};
            String[] names2Kaz = {"Лиссабон", "Лейрия"};
            Country country2 = null;

            country2 = cDao.findByName("portugal");

            for (int i = 0; i < data2.length; i++) {
                Region entity = new Region();
                entity.setCountry(country2);
                entity.setName(data2[i]);
                Map<LanguageCode, String> name = new HashMap<>();
                name.put(LanguageCode.RUS, names2Rus[i]);
                name.put(LanguageCode.ENG, names2Eng[i]);
                name.put(LanguageCode.KAZ, names2Kaz[i]);
                entity.setLocName(name);
                RegionTypeDAO rtDao = new RegionTypeDAO(ses);
                RegionType rType = null;
                if (data2[i].equals("lisbon")) {
                    rType = rtDao.findByCode(RegionCode.URBAN_AGGLOMERATION);
                } else {
                    rType = rtDao.findByCode(RegionCode.REGION);
                }
                entity.setType(rType);
           //     entities.add(entity);
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        try {
            RegionDAO dao = new RegionDAO(ses);
            for (Region entry : entities) {
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

            if (dao.findPrimary() == null) {
                Region region = dao.findByName("pavlodar_region");
                //Region region = dao.getRandomEntity();
                region.setPrimary(true);
                dao.update(region);
            }

        } catch (DAOException e) {
            logger.exception(e);
        } catch (SecureException e) {
            logger.exception(e);
        }
        logger.info("done...");
    }

}
