package discussing.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;

import discussing.model.Topic;

public class TopicDAO extends DAO<Topic, UUID> {

	public TopicDAO(_Session session) throws DAOException {
		super(Topic.class, session);
	}
	
}
