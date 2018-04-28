package reference.task;

import administrator.dao.IntegrationHubServiceDAO;
import administrator.dao.IntegrationHubCollationDAO;
import administrator.model.IntegrationHubCollation;
import administrator.model.IntegrationHubService;
import com.exponentus.appenv.AppEnv;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.integrationhub.Helper;
import com.exponentus.integrationhub.HubEnvConst;
import com.exponentus.extconnect.exception.RequesterException;
import com.exponentus.integrationhub.HubRequester;
import com.exponentus.integrationhub.IHRequester;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.util.ReflectionUtil;
import reference.init.ModuleConst;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//run task ref_sync_with_ih
@Command(name = ModuleConst.CODE + "_sync_with_ih")
public class SyncWithIntegrationHub extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        try {
            IntegrationHubServiceDAO serviceDAO = new IntegrationHubServiceDAO(ses);
            IntegrationHubCollationDAO collationDAO = new IntegrationHubCollationDAO(ses);
            for (Map.Entry<String, String> e : Helper.referenceDataInitSequence().entrySet()) {
                IntegrationHubService service = serviceDAO.findByName(e.getKey());
                if (service != null) {
                    IntegrationHubCollation collation = collationDAO.findByServiceName(service);
                    if (collation != null) {
                        IDAO<IAppEntity<UUID>, UUID> dao = DAOFactory.get(ses, collation.getEntityClassName());
                        IHRequester requester = new HubRequester(Environment.integrationHubHost, HubEnvConst.MODULE_NAME);
                        List<Map<String, ?>> data = requester.getData(service.getServiceUrl(), 0, 0);
                        Iterator iterator = data.iterator();

                        while (iterator.hasNext()) {
                            Map entry = (Map) iterator.next();
                            //    String extId = (String) entry.get("identifier");
                            String extId = (String) entry.get("id");
                            try {
                                IAppEntity entity = dao.findByExtKey(extId);
                                if (entity == null) {
                                    entity = ReflectionUtil.getEmptyInstance((Class<IAppEntity<UUID>>) Class.forName(collation.getEntityClassName()));
                                }

                                if(entity.compose(ses, entry)) {
                                    save(dao, entity, extId);
                                }else{
                                    Lg.error("Entity " + entity.getId() + " has not composed and skipped");
                                }
                            } catch (ClassNotFoundException error) {
                                Lg.exception(error);
                            }
                        }
                    } else {
                        Lg.error("Collation for  \"" + service.getName() + "\", has not been found");
                    }
                } else {
                    Lg.error("IntegrationBus service \"" + e.getKey() + "\", has not been found");
                }
            }

        } catch (RequesterException e) {
            Lg.exception(e);
        } catch (DAOException e) {
            Lg.exception(e);
        }
        logger.info("done...");
    }

    protected void save(IDAO dao, IAppEntity<UUID> entity, String extKey) {
        try {
            dao.save(entity, extKey);
        } catch (DAOException e) {
            if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                Lg.warning("a data is already exists (" + e.getAddInfo() + ", record was skipped");
            } else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
                Lg.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
            } else {
                Lg.exception(e);
            }
        } catch (SecureException e) {
            Lg.exception(e);
        }
    }

}
