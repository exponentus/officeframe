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
import reference.dao.ProjectStageDAO;
import reference.init.ModuleConst;
import reference.model.ProductType;
import reference.model.ProjectStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_project_stages")
public class FillProjectStages extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<ProjectStage> entities = new ArrayList<>();

        String names[] = {"idea", "draft_design", "working_project", "experimental_party", "current_production"};
        String namesEng[] = {"Idea", "Draft design", "Working project", "Experimental party", "Current production"};
        String namesRus[] = {"Идея", "Эскизный проект", "Рабочий проект", "Опытная партия", "Действующее производство"};
        String namesKaz[] = {"Идея", "Жобалау дизайны", "Жұмыс жобасы", "Экспериментальный партия", "Ағымдағы өндіріс"};
        for (int i = 0; i < names.length; i++) {
            ProjectStage entity = new ProjectStage();
            entity.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            ProjectStageDAO dao = new ProjectStageDAO(ses);
            for (ProjectStage entry : entities) {
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
