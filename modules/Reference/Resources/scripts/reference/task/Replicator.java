package reference.task;

import administrator.dao.CollationDAO;
import administrator.dao.IntegrationHubCollationDAO;
import administrator.dao.IntegrationHubServiceDAO;
import administrator.model.Collation;
import administrator.model.IntegrationHubCollation;
import administrator.model.IntegrationHubService;
import com.exponentus.appenv.AppEnv;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.extconnect.exception.RequesterException;
import com.exponentus.integrationhub.HubEnvConst;
import com.exponentus.integrationhub.HubRequester;
import com.exponentus.integrationhub.IHRequester;
import com.exponentus.log.Lg;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.util.ReflectionUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kaira on 4/30/18.
 */
public class Replicator extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        try {
            SortParams sortParams = SortParams.desc("regDate");
            IntegrationHubServiceDAO hubServiceDAO = new IntegrationHubServiceDAO(ses);
            ViewPage<IntegrationHubService> vp = hubServiceDAO.findViewPage(sortParams, 1, 100);
            for (IntegrationHubService service : vp.getResult()) {
                if (service != null) {
                    IntegrationHubCollationDAO hubCollationDAO = new IntegrationHubCollationDAO(ses);
                    IntegrationHubCollation collation = hubCollationDAO.findByService(service);
                    if (collation != null) {
                        IDAO<IAppEntity<UUID>, UUID> dao = DAOFactory.get(ses, collation.getEntityClassName());
                        IHRequester requester = new HubRequester(Environment.integrationHubHost, HubEnvConst.MODULE_NAME);
                        List<Map<String, ?>> data = requester.getData(service.getServiceUrl(), 0, 0);
                        if (data != null) {
                            Iterator iterator = data.iterator();
                            CollationDAO collationDAO = new CollationDAO(ses);
                            while (iterator.hasNext()) {
                                Map entry = (Map) iterator.next();
                                //  String extId = (String) entry.get("identifier");
                                String extId = (String) entry.get("id");
                                IAppEntity entity = dao.findByExtKey(extId);
                                if (entity == null) {
                                    entity = ReflectionUtil.getEmptyInstance((Class<IAppEntity<UUID>>) Class.forName(collation.getEntityClassName()));
                                }
                                if (entity.compose(ses, entry)) {
                                    save(collationDAO, dao, entity, extId);
                                } else {
                                    Lg.error("Entity " + entity.getId() + " has not composed and skipped");
                                }
                            }
                        } else {
                            Lg.error("No data from  \"" + service.getName() + "\"");
                        }
                    } else {
                        Lg.error("Collation for  \"" + service.getName() + "\", has not been found");
                    }
                }
            }
        } catch (RequesterException e) {
            Lg.error(e);
        } catch (DAOException e) {
            Lg.error(e);
        } catch (ClassNotFoundException e) {
            Lg.error(e);
        }
    }

    protected void save(CollationDAO cDao, IDAO dao, IAppEntity<UUID> entity, String extKey) {
        try {
            if (entity.getId() == null) {
                if (dao.add(entity) != null) {
                    Collation collation = new Collation();
                    collation.setExtKey(extKey);
                    collation.setIntKey(entity.getId());
                    collation.setEntityType(entity.getClass().getName());
                    cDao.add(collation);
                    Lg.info(entity.getId() + " added");
                }
            } else {
                if (dao.update(entity) != null) {
                    Lg.info(entity.getId() + " updated");
                }
            }
        } catch (DAOException e) {
            if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                Lg.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
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
