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
import reference.model.Country;
import reference.model.constants.CountryCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "fill_countries")
public class FillCountries extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<Country> entities = new ArrayList<Country>();
        String[] data = {"kazakhstan", "russia", "byelorussia", "ukraine", "germany", "france", "turkey", "usa",
                "china", "bulgaria", "portugal"};
        String[] dataRus = {"Казахстан", "Россия", "Беларуссия", "Украина", "Германия", "Франция", "Турция", "США",
                "Китай", "Болгария", "Португалия"};
        String[] dataKaz = {"Қазақстан", "Ресей", "Беларусь", "Украина", "Германия", "Франция", "Түркия", "АҚШ",
                "Қытай", "Болгария", "Португалия"};
        String[] dataEng = {"Kazakhstan", "Russia", "Byelorussia", "Ukraine", "Germany", "France", "Turkey", "USA",
                "China", "Bulgaria", "Portugal"};
        CountryCode[] code = {CountryCode.KZ, CountryCode.RU, CountryCode.BY, CountryCode.UA, CountryCode.DE,
                CountryCode.FR, CountryCode.TR, CountryCode.US, CountryCode.CN, CountryCode.BG, CountryCode.PT};

        for (int i = 0; i < data.length; i++) {
            Country entity = new Country();
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
            CountryDAO dao = new CountryDAO(ses);
            for (Country entry : entities) {
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
