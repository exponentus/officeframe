package calendar.task;

import calendar.init.AppConst;
import calendar.model.Event;
import calendar.model.Reminder;
import com.exponentus.appenv.AppEnv;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Command(name = AppConst.CODE + "_backup")
public class Backup extends Do {
    private ObjectMapper mapper = new ObjectMapper();

    public void doTask(AppEnv appEnv, _Session ses) {
        backup(ses, Reminder.class);
        backup(ses, Event.class);
    }

    private void backup(_Session ses, Class class1) {
        System.out.println("Backup " + class1.getSimpleName() + "...");
        final File tmp = new File(EnvConst.BACKUP_DIR  + File.separator + class1.getSimpleName());
        if (!tmp.exists()) {
            tmp.mkdir();
        }

        IDAO<IAppEntity, UUID> dao = DAOFactory.get(ses, class1.getCanonicalName());
        ViewPage<IAppEntity> vp = dao.findAll();
        System.out.println("Found " + vp.getCount() + " records");
        for (IAppEntity entity : vp.getResult()) {
            FileWriter writer = getFile(class1, tmp.getAbsolutePath(), entity.getId().toString());
            try {
                System.out.println(">write " + entity.getId());
                writer.append(mapper.writeValueAsString(entity));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

       /* try {
            pack(tmp.getAbsolutePath(), tmp.getAbsolutePath() + ".zip");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        System.out.println(class1.getName() + " backup was created successfully");

    }

    private FileWriter getFile(Class<?> class1, String baseDir, String id) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(baseDir + File.separator + id + "_" + class1.getName() + "_.json");
            return fileWriter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }

}
