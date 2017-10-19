package dataexport.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.util.ReflectionUtil;
import dataexport.dao.ReportProfileDAO;
import dataexport.init.AppConst;
import dataexport.model.ReportProfile;
import dataexport.model.constants.ExportFormatType;
import dataexport.model.constants.ReportQueryType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

@Command(name = AppConst.CODE + "_gen_default_reports")
public class GenerateDefaultReportProfiles extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        try {
            ReportProfileDAO dao = new ReportProfileDAO(ses);

            for (AppEnv env : Environment.getApplications()) {
                System.out.println(env.appName);
                for (Class<IAppEntity<UUID>> entity : ReflectionUtil.getAllAppEntities(env.getPackageName())) {
                    System.out.println(entity.getCanonicalName());
                    ReportProfile reportProfile = new ReportProfile();
                    reportProfile.setName(entity.getSimpleName());
                    reportProfile.setReportQueryType(ReportQueryType.ENTITY_REQUEST);
                    reportProfile.setClassName(entity.getCanonicalName());
                    reportProfile.setOutputFormat(ExportFormatType.PDF);
                    LocalDate firstDay = LocalDate.now().with(firstDayOfYear());
                    reportProfile.setStartFrom(Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    reportProfile.setEndUntil(new Date());
                    dao.add(reportProfile);
                }
            }
        } catch (DAOException | SecureException e) {
            logger.exception(e);
        }

        logger.info("done...");
    }
}
