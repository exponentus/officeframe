package discussing.dao;

import java.util.UUID;

import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;

import discussing.model.Topic;

public class TopicDAO extends DAO<Topic, UUID> {

	public TopicDAO(_Session session) {
		super(Topic.class, session);
	}

}
