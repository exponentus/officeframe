package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.init.AppConst;
import reference.model.Locality;
import reference.model.Street;

import java.util.ArrayList;
import java.util.List;

@Command(name = AppConst.CODE + "_fill_streets")
public class FillStreets extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<Street> entities = new ArrayList<Street>();

        try {
            LocalityDAO cDao = new LocalityDAO(ses);
            Locality d = cDao.findAll().getFirstEntity();
            if (d != null) {
                String[] data = {"Champs Elysées", "La Rambla", "Fifth Avenue", "Via Appia", "Zeil", "Abbey Road",
                        "Khao San", "Rua Augusta"};
                int count = 1;
                for (String val : data) {
                    Street entity = new Street();
                    entity.setLocality(d);
                    entity.setName(val);
                    entity.setStreetId(count);
                    entities.add(entity);
                    count++;
                }
            }else{
                Lg.error("There is no any Locality");

            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        try {
            StreetDAO dao = new StreetDAO(ses);
            for (Street entry : entities) {
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
