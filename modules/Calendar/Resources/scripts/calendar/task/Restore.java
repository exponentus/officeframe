package calendar.task;

import calendar.dao.EventDAO;
import calendar.dao.ReminderDAO;
import calendar.init.ModuleConst;
import calendar.model.Event;
import calendar.model.Reminder;
import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

//run task cal_restore
@Command(name = ModuleConst.CODE + "_restore")
public class Restore extends Do {
    private ObjectMapper mapper = new ObjectMapper();

    public void doTask(AppEnv appEnv, _Session ses) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        restoreReminder(ses);
        restoreEvent(ses);
    }


    private <T extends IAppEntity<UUID>> void restoreReminder(_Session ses) {
        try {
            ReminderDAO dao = new ReminderDAO(ses);
            File dir = new File(EnvConst.BACKUP_DIR + File.separator + Reminder.class.getSimpleName());
            if (dir.isDirectory()) {
                File[] list = dir.listFiles();
                for (int i = list.length; --i >= 0; ) {
                    try {
                        Reminder entity = mapper.readValue(new File(list[i].getAbsolutePath()), Reminder.class);
                        Reminder newEntity = new Reminder();
                        newEntity.setId(entity.getId());
                        newEntity.setTitle(entity.getTitle());
                        newEntity.setReminderType(entity.getReminderType());
                        newEntity.setDescription(entity.getDescription());
                        newEntity.setObservers(entity.getObservers());
                        dao.add(newEntity);
                        System.out.println("\"" + newEntity.getTitle() + "\" backup was restored");
                    } catch (DAOException e) {
                        if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                            logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
                        } else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
                            logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
                        } else {
                            logger.exception(e);
                        }
                    } catch (SecureException e) {
                        logger.exception(e);
                    } catch (JsonParseException e) {
                        logger.exception(e);
                    } catch (JsonMappingException e) {
                        logger.exception(e);
                    } catch (IOException e) {
                        logger.exception(e);
                    }
                }


            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private <T extends IAppEntity<UUID>> void restoreEvent(_Session ses) {
        try {
            EventDAO dao = new EventDAO(ses);
            File dir = new File(EnvConst.BACKUP_DIR + File.separator + Event.class.getSimpleName());
            if (dir.isDirectory()) {
                File[] list = dir.listFiles();
                for (int i = list.length; --i >= 0; ) {
                    try {
                        Event entity = mapper.readValue(new File(list[i].getAbsolutePath()), Event.class);
                        Event newEntity = new Event();
                        newEntity.setId(entity.getId());
                        newEntity.setTitle(entity.getTitle());
                        newEntity.setReminder(entity.getReminder());
                        newEntity.setDescription(entity.getDescription());
                        newEntity.setObservers(entity.getObservers());
                        newEntity.setPriority(entity.getPriority());
                        newEntity.setEventTime(entity.getEventTime());
                        dao.add(newEntity);
                        System.out.println("\"" + newEntity.getTitle() + "\" backup was restored");
                    } catch (DAOException e) {
                        if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                            logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
                        } else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
                            logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
                        } else {
                            logger.exception(e);
                        }
                    } catch (SecureException e) {
                        logger.exception(e);
                    } catch (JsonParseException e) {
                        logger.exception(e);
                    } catch (JsonMappingException e) {
                        logger.exception(e);
                    } catch (IOException e) {
                        logger.exception(e);
                    }
                }


            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }



}
