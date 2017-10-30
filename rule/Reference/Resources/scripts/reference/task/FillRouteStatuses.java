package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.RouteStatusDAO;
import reference.init.AppConst;
import reference.model.RouteStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_route_statuses")
public class FillRouteStatuses extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<RouteStatus> entities = new ArrayList<>();

        String names[] = {"custom", "urban_or_rural", "interregional", "suburban", "inter-district", "intercity",
                "international", "one-time", "dangerous"};
        String namesEng[] = {"Custom", "Urban or rural", "Interregional", "Suburban", "Inter-district", "Intercity",
                "International", "One-time", "Dangerous"};
        String namesRus[] = {"Заказной", "Городской или сельский", "Межобластной", "Пригородный", "Межрайонный",
                "Междугородний", "Международный", "Разовый", "Опасный"};
        String namesKaz[] = {"Пайдаланушы", "Қалалық немесе Аудандар", "Аймақаралық", "Қала маңы", "Ауданаралық",
                "Қалааралық", "Халықаралық", "Бір жолғы", "Қауіпті"};

        for (int i = 0; i < names.length; i++) {
            RouteStatus entity = new RouteStatus();
            entity.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            RouteStatusDAO dao = new RouteStatusDAO(ses);
            for (RouteStatus entry : entities) {
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
