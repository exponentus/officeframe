package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.ProductTypeDAO;
import reference.dao.VehicleDAO;
import reference.init.ModuleConst;
import reference.model.ProductType;
import reference.model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_product_types")
public class FillProductTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<ProductType> entities = new ArrayList<>();

        String names[] = {"trabant", "scania", "gaz", "niva"};
        String namesEng[] = {"Trabant", "Scania", "Gaz", "Niva"};
        String namesRus[] = {"Трабант", "Скания", "Газ-24", "Нива Ваз-2121"};
        String namesKaz[] = {"Трабант", "Скания", "Газ-24", "Нива Ваз-2121"};
        for (int i = 0; i < names.length; i++) {
            ProductType entity = new ProductType();
            entity.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            ProductTypeDAO dao = new ProductTypeDAO(ses);
            for (ProductType entry : entities) {
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