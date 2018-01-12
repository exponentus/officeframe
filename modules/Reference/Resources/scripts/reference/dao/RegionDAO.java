package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Country;
import reference.model.Region;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class RegionDAO extends ReferenceDAO<Region, UUID> {

    public RegionDAO(_Session session) throws DAOException {
        super(Region.class, session);
    }

    public Region findPrimary() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery("SELECT e FROM " + getEntityClass().getName()
                    + " AS e WHERE e.isPrimary = TRUE");
            return (Region) query.getResultList().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public List<Region> findAllByCountry(Country country) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Region> cq = cb.createQuery(getEntityClass());
            Root<Region> c = cq.from(Region.class);
            Predicate condition = cb.equal(c.get("country"), country);
            cq.select(c).where(condition);

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
