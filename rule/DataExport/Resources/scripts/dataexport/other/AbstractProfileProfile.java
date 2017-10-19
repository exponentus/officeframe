package dataexport.other;

import com.exponentus.common.other.IReportProfile;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by kaira on 9/9/17.
 */
public abstract class AbstractProfileProfile implements IReportProfile {
    protected _Session session;

    @Override
    public void setSession(_Session session){
        this.session = session;
    }

    @Override
    public abstract String getTemplateName();


    @Override
    public abstract String getAppCode();

    public String getReportFileName(){
        Date current = new Date();
        return getTemplateName() + "_" + TimeUtil.dateTimeToFileNameSilently(current);
    }

    @Override
    public abstract List getReportData(Date from, Date until, String customParameter);

}
