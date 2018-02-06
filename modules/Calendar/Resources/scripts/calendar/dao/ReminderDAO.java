package calendar.dao;

import calendar.model.Reminder;
import calendar.model.constants.ReminderType;
import com.exponentus.common.dao.DAO;
import com.exponentus.common.model.embedded.Reader;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import org.apache.poi.ss.formula.functions.T;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.UUID;

public class ReminderDAO extends DAO<Reminder, UUID> {

    public ReminderDAO(_Session session) throws DAOException {
        super(Reminder.class, session);
    }

    public Reminder getDefault() throws DAOException, SecureException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        if (user.isSuperUser()) {
            throw new DAOException(DAOExceptionType.LIMITATION_OF_USING_VIRTUAL_USER, user.getLogin());
        } else {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            try {
                CriteriaQuery<Reminder> cq = cb.createQuery(Reminder.class);
                Root<Reminder> c = cq.from(Reminder.class);
                cq.select(c);
                Predicate condition = c.get("reminderType").in(ReminderType.SILENT);
                MapJoin<T, Long, Reader> mapJoin = c.joinMap("readers");
                condition = cb.and(cb.equal(mapJoin.key(), user.getId()), condition);
                cq.where(condition);
                Query query = em.createQuery(cq);
                Reminder entity = (Reminder) query.getSingleResult();
                if (entity.getEditors().contains(user.getId())) {
                    entity.setEditable(true);
                }
                return entity;
            } catch (NoResultException e) {
                Reminder reminder = new Reminder();
                reminder.setReminderType(ReminderType.SILENT);
                reminder.setTitle("default silent reminder");
                reminder.setDescription("this document generated automaticaly");
                add(reminder);
                return reminder;
            } catch (PersistenceException e) {
                throw new DAOException(e);
            } catch (Exception e) {
                Server.logger.exception(e);
                return null;
            } finally {
                em.close();
            }
        }
    }
}
