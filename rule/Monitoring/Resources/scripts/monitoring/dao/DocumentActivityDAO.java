package monitoring.dao;

import com.exponentus.common.dao.SimpleDAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import monitoring.model.DocumentActivity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class DocumentActivityDAO extends SimpleDAO<DocumentActivity> {

    public DocumentActivityDAO(_Session ses) {
        super(DocumentActivity.class);
    }

    public DocumentActivity findByEntityId(UUID entityId) throws DAOException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<DocumentActivity> cq = cb.createQuery(DocumentActivity.class);
            Root<DocumentActivity> c = cq.from(DocumentActivity.class);
            cq.select(c);
            Predicate condition = cb.equal(c.get("actEntityId"),entityId);
            cq.where(condition);
            TypedQuery<DocumentActivity> typedQuery = em.createQuery(cq);
            return typedQuery.getSingleResult();
        } catch (NonUniqueResultException e) {
            throw new DAOException(e);
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
