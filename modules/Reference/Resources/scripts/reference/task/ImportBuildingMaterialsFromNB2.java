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
import reference.dao.BuildingMaterialDAO;
import reference.model.BuildingMaterial;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

//run task import_building_materials_nb2
@Command(name = "import_building_materials_nb2")
public class ImportBuildingMaterialsFromNB2 extends ImportNB2 {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {

        try {
            Map<String, BuildingMaterial> buildingMaterialMap = new HashMap<>();
            BuildingMaterialDAO buildingMaterialDAO = new BuildingMaterialDAO(ses);
            IDBConnectionPool dbPool = getConnectionPool();
            Connection conn = dbPool.getConnection();
            conn.setAutoCommit(false);
            Statement s = conn.createStatement();
            String sql = "select g.docid, g.parentdocid, g.ddbid, g.form, g.viewtext3, g.viewtext2 from glossary g where g.form ='material'";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                String unId = rs.getString("ddbid");
                BuildingMaterial entity = buildingMaterialDAO.findByExtKey(unId);
                if (entity == null) {
                    entity = new BuildingMaterial();
                    entity.setAuthor(new SuperUser());
                }

                String kazName = rs.getString("viewtext2");
                String rusName = rs.getString("viewtext3");
                entity.setName(StringUtil.convertStringToURL(rusName));
                Map<LanguageCode, String> localizedNames = new HashMap<>();
                localizedNames.put(LanguageCode.RUS, rusName);
                localizedNames.put(LanguageCode.KAZ, kazName);
                entity.setLocName(localizedNames);
                entity.setTitle(rusName);
                buildingMaterialMap.put(unId, entity);

            }

            logger.info("has been found " + buildingMaterialMap.size() + " records");


            for (Map.Entry<String, BuildingMaterial> entry : buildingMaterialMap.entrySet()) {
                save(buildingMaterialDAO, entry.getValue(), entry.getKey());
            }

            logger.info("done...");
        } catch (SQLException e) {
            DatabaseUtil.errorPrint(e);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

}
