package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.TagDAO;
import reference.model.Tag;

import java.util.List;

@Command(name = "st2")
public class SpeedChecker extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session session) {
        System.out.println("run...");
        long start_time = System.nanoTime();
        int iteration0 = 10;
        int iteration = 500;
        try {
            for (int i0 = 0; i0 < iteration0; i0++) {
                TagDAO dao = new TagDAO(session);
                int cached = 0, notCached = 0;
                List<Tag> list = null;
                for (int i = 0; i < iteration; i++) {
                    cached = 0;
                    notCached = 0;
                    list = dao.findAll();
                    for (Tag tag : list) {
                        if (dao.isCached(tag)) {
                            cached++;
                        } else {
                            notCached++;
                        }
                    }
                }
                System.out.println(i0 + " cached=" + cached + ", not cached=" + notCached);
                dao.resetCache();
                cached = 0;
                notCached = 0;
                for (Tag tag : list) {
                    if (dao.isCached(tag)) {
                        cached++;
                    } else {
                        notCached++;
                    }
                }
                System.out.println("  cached=" + cached + ", not cached=" + notCached);
            }

        } catch (Exception e) {
            System.err.println(e);
        }

        long end_time = System.nanoTime();
        System.out.println("done");
        System.out.println("speed=" + (end_time - start_time) / 1e6);
    }
}
