package calendar.dao;

import calendar.model.Event;
import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventDAO extends DAO<Event, UUID> {

    public EventDAO(_Session session) throws DAOException {
        super(Event.class, session);
    }

    public List<Event> findEventsBetween(Date begin, Date end) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Event> q = em.createQuery("SELECT m FROM Event AS m WHERE m.eventTime >= :begin AND m.eventTime < :end", Event.class);
            q.setParameter("begin", begin, TemporalType.TIMESTAMP);
            q.setParameter("end", end, TemporalType.TIMESTAMP);

            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
