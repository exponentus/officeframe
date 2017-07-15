package discussing.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import discussing.model.Topic;

import java.util.UUID;

public class TopicDAO extends DAO<Topic, UUID> {

	public TopicDAO(_Session session) throws DAOException {
		super(Topic.class, session);
	}
	
}
