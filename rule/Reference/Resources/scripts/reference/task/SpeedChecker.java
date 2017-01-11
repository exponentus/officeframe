package reference.task;

import java.util.List;

import com.exponentus.appenv.AppEnv;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.TagDAO;
import reference.model.Tag;

@Command(name = "st2")
public class SpeedChecker extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session session) {
		System.out.println("run...");
		long start_time = System.nanoTime();
		try {
			int iteration = 5000;
			for (int i = 0; i < iteration; i++) {
				TagDAO dao = new TagDAO(session);
				List<Tag> list = dao.findAll().getResult();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		
		long end_time = System.nanoTime();
		System.out.println("done");
		System.out.println("speed=" + (end_time - start_time) / 1e6);
		
	}

}
