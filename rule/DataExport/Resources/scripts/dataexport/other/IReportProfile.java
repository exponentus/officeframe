package dataexport.other;

import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.scripting._Session;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by kaira on 9/9/17.
 */
public interface IReportProfile<T extends IAppEntity<UUID>> {

    void setSession(_Session session);

    String getTemplateName();

    String getAppCode();

    String getReportFileName();

    List<T> getReportData(Date from, Date until, String customParameter);

}
