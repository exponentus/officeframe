package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.PropertyCodeDAO;
import reference.init.ModuleConst;
import reference.model.PropertyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Права пользования
 * <p>
 * Created by Kayra on 30/12/15.
 */

@Command(name = ModuleConst.CODE + "_fill_property_codes")
public class FillPropertyCodes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<PropertyCode> entities = new ArrayList<PropertyCode>();
        String[] data = {"gratuitous_use", "temporary_free-of-charge_use", "temporary_gratuitous_use",
                "trust_management", "long-term_lease", "concession", "operational_management", "resolution_on_the_establishment", "order",
                "ownership", "economic_management"};
        String[] dataEng = {"Gratuitous use", "Temporary free-of-charge use", "Temporary gratuitous use",
                "Trust management", "Long-term lease", "Concession", "Operational management", "Resolution on the establishment", "Order",
                "Ownership", "Economic Management"};
        String[] dataRus = {"Безвозмездное пользование", "Временное безвоздмездное пользование", "Временное безвозмездное пользование",
                "Доверительное управление", "Долгосрочная аренда", "Концессия", "Оперативное управление", "Постановление о создании", "Распоряжение",
                "Собственность", "Хозяйственное ведение"};
        String[] dataKaz = {"Ақысыз пайдалану", "Уақытша тегін пайдалану", "Уақытша өтеусіз пайдалану",
                "Сенімді басқару", "Ұзақ мерзімді жалдау", "Концессия", "Операциялық басқару", "Орнату туралы шешім", "Тапсырыс",
                "Меншік", "Экономикалық менеджмент"};

        for (int i = 0; i < data.length; i++) {
            PropertyCode entity = new PropertyCode();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            PropertyCodeDAO dao = new PropertyCodeDAO(ses);
            for (PropertyCode entry : entities) {
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
