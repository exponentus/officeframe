package staff.services.helper;

import com.exponentus.appenv.AppEnv;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.dataengine.jpa.ISecureAppEntity;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Role;

import java.util.UUID;

/**
 * Created by kaira on 9/9/17.
 */
public class RoleProcessor {
    private Employee emp;
    private _Session ses;

    public RoleProcessor(_Session ses, Employee emp){
        Lg.info("Check roles and process");
        this.emp = emp;
        this.ses = ses;
    }



    public void checkSupervisorRole() throws SecureException, DAOException {
        for(AppEnv env:Environment.getApplications()){
            Role role = new RoleDAO(ses).findByName(env.appCode + EnvConst.SUPERVISOR_ROLE_NAME);
            if (role != null) {
                if (emp.getRoles().contains(role)) {
                    for (String clazzName : env.getAllEntities()) {
                        IDAO<ISecureAppEntity, UUID> dao = DAOFactory.get(ses, clazzName);
                        ViewPage<ISecureAppEntity> vp = dao.findAll();
                        for (ISecureAppEntity entity : vp.getResult()) {
                            entity.addReader(emp.getUser());
                            Lg.info(emp.getLogin() + " added as reader to: " + entity.getEntityKind() + " " + entity.getId());
                            dao.update(entity);
                        }
                    }
                }
            }
        }
    }
}
