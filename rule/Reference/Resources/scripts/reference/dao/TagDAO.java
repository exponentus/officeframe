package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Tag;

import java.util.UUID;

public class TagDAO extends ReferenceDAO<Tag, UUID> {

    public TagDAO(_Session session) throws DAOException {
        super(Tag.class, session);
    }
}
