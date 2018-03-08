package staff.task;

import administrator.dao.UserDAO;
import administrator.model.User;
import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.IDBConnectionPool;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jdbc.DatabaseUtil;
import com.exponentus.exception.SecureException;
import com.exponentus.legacy.forms.Import4MS;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.user.SuperUser;
import com.exponentus.user.constants.UserStatusCode;
import staff.dao.EmployeeDAO;
import staff.model.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

//run task import_emp_nb2
@Command(name = "import_emp_nb2")
public class ImportEmployeeFromNB2 extends Import4MS {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        Map<String, Employee> employeeMap = new HashMap<>();
        Map<String, User> userMap = new HashMap<>();
        IDBConnectionPool dbPool = getConnectionPool();
        Connection conn = dbPool.getConnection();
        try {
            EmployeeDAO eDao = new EmployeeDAO(ses);
            UserDAO uDao = new UserDAO(ses);

            conn.setAutoCommit(false);
            Statement s = conn.createStatement();
            String sql = "select e.regdate, e.lastupdate, e.viewtext, e.fullname, e.shortname, e.userid, e.phone, e.birthdate, e.viewtext,  e.viewtext1, e.viewtext2, e.viewtext3, e.viewnumber  from employers e ";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                String unId = rs.getString("userid");
                Employee entity = eDao.findByExtKey(unId);
                if (entity == null) {
                    entity = new Employee();
                    entity.setAuthor(new SuperUser());
                }
                User user = (User) uDao.findByExtKey(unId);
                if (user == null) {
                    user = new User();
                    entity.setAuthor(new SuperUser());
                }

                user.setLogin(unId);
                user.setDefaultLang(LanguageCode.RUS);
                user.setStatus(UserStatusCode.REGISTERED);
                user.setPwd("123");
                user.setExtKey(unId);

                entity.setUser(user);
                entity.setName(rs.getString("fullname"));
                entity.setPhone(rs.getString("phone"));
                entity.setBirthDate(rs.getDate("birthdate"));
                entity.setTitle(rs.getString("viewtext"));

                employeeMap.put(unId, entity);
                userMap.put(unId, user);

            }

            logger.info("has been found " + employeeMap.size() + " records");


            for (Map.Entry<String, User> entry : userMap.entrySet()) {
                try {
                    uDao.save(entry.getValue());
                } catch (SecureException e) {
                    e.printStackTrace();
                }
            }

            for (Map.Entry<String, Employee> entry : employeeMap.entrySet()) {
                save(eDao, entry.getValue(), entry.getKey());
            }

            logger.info("done...");
        } catch (SQLException e) {
            DatabaseUtil.errorPrint(e);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
