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
import reference.dao.OperativeInfoTypeDAO;
import reference.dao.PositionDAO;
import reference.init.ModuleConst;
import reference.model.OperativeInfoType;
import reference.model.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//run task ref_fill_operative_info_types
@Command(name = ModuleConst.CODE + "_fill_operative_info_types")
public class FillOperativeInfoTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<OperativeInfoType> entities = new ArrayList<OperativeInfoType>();
        String[] data = {"weather","internet_recource","news","faq"};
        String[] dataEng = {"Weather","Internet resource", "News", "FAQ"};
        String[] dataRus = {"Погода","Интернет-ресурс", "Новость","Вопрос-ответ"};
        String[] dataKaz = {"Ауа райы","Интернет-ресурс", "Жаңалықтар", "Сұрақ-жауап"};

        int[] rankData = {999};

        for (int i = 0; i < data.length; i++) {
            OperativeInfoType entity = new OperativeInfoType();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            entity.setLocName(name);
            entity.setRank(rankData[0]);
            entities.add(entity);
        }

        try {
            OperativeInfoTypeDAO dao = new OperativeInfoTypeDAO(ses);
            for (OperativeInfoType entry : entities) {
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
