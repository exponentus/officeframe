package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.TextTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

public class TextTemplateDAO extends ReferenceDAO<TextTemplate, UUID> {

    public TextTemplateDAO(_Session session) throws DAOException {
        super(TextTemplate.class, session);
    }

    public List<TextTemplate> findAllCategories() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery("SELECT e.category FROM TextTemplate AS e GROUP BY e.category");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
