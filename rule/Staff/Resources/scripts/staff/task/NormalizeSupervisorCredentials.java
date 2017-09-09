package staff.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;

@Command(name = "normalize_supervisor_credentials")
public class NormalizeSupervisorCredentials extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("done...");
	}

}
