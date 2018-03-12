package reference.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.AsOf;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class AsOfDAO extends DAO<AsOf, UUID> {

    public AsOfDAO(_Session session) throws DAOException {
        super(AsOf.class, session);
    }

    public AsOf findByName(String name) throws DAOException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<AsOf> cq = cb.createQuery(getEntityClass());
            Root<AsOf> c = cq.from(getEntityClass());
            cq.select(c);
            Predicate condition = cb.equal(cb.lower(c.get("name")), name.toLowerCase());
            cq.where(condition);
            TypedQuery<AsOf> typedQuery = em.createQuery(cq);
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
