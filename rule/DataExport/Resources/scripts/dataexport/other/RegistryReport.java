package dataexport.other;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.scripting._Session;
import com.exponentus.util.StringUtil;
import dataexport.init.AppConst;

import java.util.Date;
import java.util.List;

/**
 * Created by kaira on 9/9/17.
 */
public class RegistryReport implements ICustomReport {
    private _Session session;

    @Override
    public void setSession(_Session session){
        this.session = session;
    }

    @Override
    public String getTemplateName() {
        return "entity_registry";
    }

    @Override
    public String getAppCode() {
        return AppConst.CODE;
    }

    @Override
    public String getReportFileName() {
        return StringUtil.generateRndAsText("qwertyuiopasdfghjklzxcvbnm", 10);
    }

    @Override
    public List getReportData(Date from, Date until, String customParameter) {
        IDAO dao = DAOFactory.get(session, customParameter);
        return dao.findAll().getResult();
    }
}
