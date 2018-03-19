package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.DataengineConst;
import com.exponentus.dataengine.IDBConnectionPool;
import com.exponentus.dataengine.exception.DatabasePoolException;
import com.exponentus.dataengine.jdbc.DBConnectionPool;
import com.exponentus.dataengine.jdbc.DatabaseUtil;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.constants.Trigger;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.server.Server;
import com.exponentus.server.cli.Info;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.concurrent.*;

//run task _ih_speed_check
@Command(name = "_ih_speed_check", trigger = Trigger.EVERY_HOUR)
public class IHSpeedChecker extends Do {
    String connectionURL = "jdbc:postgresql://" + EnvConst.DATABASE_HOST + ":" + EnvConst.CONN_PORT + "/" + EnvConst.DATABASE_NAME;
    private static  final int TIME_OUT_SECS = 10;

    @Override
    public void doTask(AppEnv appEnv, _Session session) {
        if (Environment.integrationHubEnable) {
            Lg.debug("connect to IH database...");
            long start_time = System.nanoTime();


            final Duration timeout = Duration.ofSeconds(TIME_OUT_SECS);
            ExecutorService executor = Executors.newSingleThreadExecutor();

            final Future<String> handler = executor.submit(new Callable() {
                @Override
                public String call() throws Exception {
                    checkServer();
                    return "";
                }
            });

            try {
                handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                handler.cancel(true);
                Lg.error("TimeoutException " + TIME_OUT_SECS + " sec");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            executor.shutdownNow();
            long end_time = System.nanoTime();
            Lg.debug("speed=" + Info.showDatabaseSpeed(((end_time - start_time) / 1e6)));



            System.out.println("done");


        }

    }

    private void checkServer(){
        IDBConnectionPool dbPool = new DBConnectionPool();
        try {
            dbPool.initConnectionPool(DataengineConst.JDBC_DRIVER, connectionURL, EnvConst.DB_USER, EnvConst.DB_PWD);
            try {
                Connection conn = dbPool.getConnection();
                conn.setAutoCommit(false);
                Statement s = conn.createStatement();
                String sql = "SELECT 1";
                ResultSet rs = s.executeQuery(sql);
                if (rs.next()) {
                    @SuppressWarnings({"unused"})
                    int n = rs.getInt(1);
                }
                s.close();
                conn.commit();
            } catch (Throwable e) {
                DatabaseUtil.debugErrorPrint(e);
            }

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | DatabasePoolException e) {
            Server.logger.exception(e);
        }
    }
}
