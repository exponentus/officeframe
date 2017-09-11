package dataexport.other;

import com.exponentus.scripting._Session;

import java.util.Date;
import java.util.List;

/**
 * Created by kaira on 9/9/17.
 */
public interface IReportProfile {

    void setSession(_Session session);

    String getTemplateName();

    String getAppCode();

    String getReportFileName();

    List getReportData(Date from, Date until, String customParameter);

}
