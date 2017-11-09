package reference.dao;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Region;
import staff.model.Organization;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
}
