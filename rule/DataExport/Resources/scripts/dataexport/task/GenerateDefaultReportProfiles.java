package dataexport.task;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.util.ReflectionUtil;
import dataexport.dao.ReportProfileDAO;
import dataexport.init.ModuleConst;
import dataexport.model.ReportProfile;
import dataexport.model.constants.ExportFormatType;
import dataexport.model.constants.ReportQueryType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

@Command(name = ModuleConst.CODE + "_gen_default_reports")
public class GenerateDefaultReportProfiles extends Do {
    private static final String REPORT_NAME_KEYWORD = "report";

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        try {
            ReportProfileDAO dao = new ReportProfileDAO(ses);

            for (AppEnv env : Environment.getApplications()) {
                System.out.println(env.appName);
                for (Class<IAppEntity<UUID>> entity : ReflectionUtil.getAllAppEntities(env.getPackageName())) {
                    System.out.println(entity.getCanonicalName());
                    String simpleName = entity.getSimpleName();
                    ReportProfile reportProfile = new ReportProfile();
                    reportProfile.setName(simpleName);
                    reportProfile.setReportQueryType(ReportQueryType.ENTITY_REQUEST);
                    reportProfile.setClassName(entity.getCanonicalName());
                    reportProfile.setOutputFormat(ExportFormatType.PDF);
                    LocalDate firstDay = LocalDate.now().with(firstDayOfYear());
                    reportProfile.setStartFrom(Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    reportProfile.setEndUntil(new Date());
                    Map<LanguageCode, String> name = new HashMap<>();
                    for (Language language : new LanguageDAO(ses).findAllActivated()) {
                        name.put(language.getCode(), Environment.vocabulary.getWord(REPORT_NAME_KEYWORD, language.getCode()) + "-" + reportProfile.getTitle());
                    }
                    reportProfile.setLocName(name);
                    dao.add(reportProfile);
                }
            }
        } catch (DAOException | SecureException e) {
            logger.exception(e);
        }

        logger.info("done...");
    }
}
