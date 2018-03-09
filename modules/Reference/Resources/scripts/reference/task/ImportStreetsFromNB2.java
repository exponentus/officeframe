package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.IDBConnectionPool;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jdbc.DatabaseUtil;
import com.exponentus.legacy.forms.ImportNB2;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;
import com.exponentus.util.StringUtil;
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.model.Locality;
import reference.model.Street;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

//run task import_streets_nb2
@Command(name = "import_streets_nb2")
public class ImportStreetsFromNB2 extends ImportNB2 {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        Map<String, Street> streetMap = new HashMap<>();
        try {
            LocalityDAO localityDAO = new LocalityDAO(ses);
            Locality locality = localityDAO.findByName("taraz");
            if (locality != null) {
                IDBConnectionPool dbPool = getConnectionPool();
                Connection conn = dbPool.getConnection();

                StreetDAO streetDAO = new StreetDAO(ses);
                conn.setAutoCommit(false);
                Statement s = conn.createStatement();
                String sql = "select g.docid, g.ddbid, g.form, g.viewtext3, g.viewtext2 from glossary g where g.form ='street'";
                ResultSet rs = s.executeQuery(sql);
                while (rs.next()) {
                    String unId = rs.getString("ddbid");
                    Street entity = streetDAO.findByExtKey(unId);
                    if (entity == null) {
                        entity = new Street();
                        entity.setAuthor(new SuperUser());
                    }

                    entity.setLocality(locality);
                    String kazName = rs.getString("viewtext2").replace(" көше", "").replace("көшесі", "");
                    String rusName = rs.getString("viewtext3").replace("улица", "");
                    entity.setName(rs.getString("viewtext3"));
                    String latName = StringUtil.convertRusToLat(rusName);
                    entity.setName(StringUtil.convertToURLString(rusName));
                    entity.setStreetId(rs.getInt("docid"));
                    Map<LanguageCode, String> localizedNames = new HashMap<>();
                    localizedNames.put(LanguageCode.ENG, latName);
                    localizedNames.put(LanguageCode.RUS, rusName);
                    localizedNames.put(LanguageCode.KAZ, kazName);
                    entity.setLocName(localizedNames);
                    entity.setTitle(rusName);
                    streetMap.put(unId, entity);
                }

                logger.info("has been found " + streetMap.size() + " records");


                for (Map.Entry<String, Street> entry : streetMap.entrySet()) {
                    save(streetDAO, entry.getValue(), entry.getKey());
                }
            } else {
                logger.error("There is not proper locality");
            }
            logger.info("done...");
        } catch (SQLException e) {
            DatabaseUtil.errorPrint(e);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
