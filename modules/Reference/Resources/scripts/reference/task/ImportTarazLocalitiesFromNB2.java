package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.IDBConnectionPool;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jdbc.DatabaseUtil;
import com.exponentus.legacy.forms.ImportNB2;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;
import com.exponentus.util.StringUtil;
import reference.dao.DistrictDAO;
import reference.dao.LocalityDAO;
import reference.dao.LocalityTypeDAO;
import reference.dao.RegionDAO;
import reference.model.District;
import reference.model.Locality;
import reference.model.LocalityType;
import reference.model.Region;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

//run task import_taraz_distr_nb2
@Command(name = "import_taraz_distr_nb2")
public class ImportTarazLocalitiesFromNB2 extends ImportNB2 {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {

        try {
            LocalityTypeDAO localityTypeDAO = new LocalityTypeDAO(ses);
            LocalityType city = localityTypeDAO.findByName("city");
            LocalityType village = localityTypeDAO.findByName("village");

            Map<Integer, District> districtMap = new HashMap<>();
            RegionDAO regionDAO = new RegionDAO(ses);
            Region region = regionDAO.findByName("zhambyl_region");
            DistrictDAO districtDAO = new DistrictDAO(ses);
            for (District district : districtDAO.findAllByRegion(region).getResult()) {
                districtMap.put(getDistrictCode(district.getName()), district);
            }
            Map<String, Locality> localityMap = new HashMap<>();
            LocalityDAO localityDAO = new LocalityDAO(ses);
            IDBConnectionPool dbPool = getConnectionPool();
            Connection conn = dbPool.getConnection();
            conn.setAutoCommit(false);
            Statement s = conn.createStatement();
            String sql = "select g.docid, g.parentdocid, g.ddbid, g.form, g.viewtext3, g.viewtext2 from glossary g where g.form ='city'";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                String unId = rs.getString("ddbid");
                Locality entity = localityDAO.findByExtKey(unId);
                if (entity == null) {
                    entity = new Locality();
                    entity.setAuthor(new SuperUser());
                }

                int districtCode = rs.getInt("parentdocid");
                District district = districtMap.get(districtCode);
                if (district != null || districtCode == 10) {
                    entity.setRegion(region);
                    if (districtCode == 10) {
                        entity.setType(city);
                    } else {
                        entity.setDistrict(district);
                        entity.setType(village);
                    }
                    String kazName = rs.getString("viewtext2").replace("a.", "").replace("с.", "");
                    String rusName = rs.getString("viewtext3").replace("a.", "").replace("с.", "");
                    String latName = StringUtil.convertRusToLat(rusName);
                    entity.setName(StringUtil.convertStringToURL(rusName));
                    Map<LanguageCode, String> localizedNames = new HashMap<>();
                    localizedNames.put(LanguageCode.ENG, latName);
                    localizedNames.put(LanguageCode.RUS, rusName);
                    localizedNames.put(LanguageCode.KAZ, kazName);
                    entity.setLocName(localizedNames);
                    entity.setTitle(rusName);
                    localityMap.put(unId, entity);
                } else {
                    Lg.warning("Dsitrict \"" + districtCode + "\" has not been found");
                }
            }

            logger.info("has been found " + localityMap.size() + " records");


            for (Map.Entry<String, Locality> entry : localityMap.entrySet()) {
                save(localityDAO, entry.getValue(), entry.getKey());
            }

            logger.info("done...");
        } catch (SQLException e) {
            DatabaseUtil.errorPrint(e);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private int getDistrictCode(String districtName) {
        switch (districtName) {

            case "baizaksky":
                return 13;
            case "talasky":
                return 39;
            case "shusky":
                return 43;
            case "zhambulsky":
                return 20;
            case "kordaysky":
                return 22;
            case "zhualynsky":
                return 21;
            case "sarysusky":
                return 35;
            case "ryskulovsky":
                return 23;
            case "moinkumsky":
                return 25;
            case "merkensky":
                return 24;
        }
        return 0;

    }

    private Map<Integer, LanguageCode> districtCollationMapInit() {
        Map<Integer, LanguageCode> depTypeCollation = new HashMap<>();
        depTypeCollation.put(1, LanguageCode.RUS);
        depTypeCollation.put(2, LanguageCode.ENG);
        depTypeCollation.put(3, LanguageCode.KAZ);
        depTypeCollation.put(null, LanguageCode.UNKNOWN);
        depTypeCollation.put(0, LanguageCode.RUS);
        return depTypeCollation;

    }
}
