package dataexport.other;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.dataengine.jpa.IDAO;
import dataexport.init.AppConst;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by kaira on 9/9/17.
 */
public class RegistryReportProfile extends AbstractProfileProfile {

    @Override
    public String getTemplateName() {
        return "entities";
    }

    @Override
    public String getAppCode() {
        return AppConst.CODE;
    }

    @Override
    public List<T> getReportData(Date from, Date until, String customParameter) {
        IDAO<T,UUID> dao = DAOFactory.get(session, customParameter);
        return dao.findAll().getResult();
    }
}
