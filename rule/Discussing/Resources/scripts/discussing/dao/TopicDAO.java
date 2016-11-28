package discussing.dao;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;

import discussing.model.Comment;
import discussing.model.Topic;

public class TopicDAO extends DAO<Topic, UUID> {
	
	public TopicDAO(_Session session) throws DAOException {
		super(Topic.class, session);
	}
	
	public ViewPage<Topic> findAllWithChildren(int pageNum, int pageSize, List<UUID> expandedIdList) {
		ViewPage<Topic> vp = findViewPage(pageNum, pageSize);
		
		for (Topic task : vp.getResult()) {
			if (task.isHasComments() && Collections.disjoint(task.getComments(), expandedIdList)) {
				
			}
		}
		
		return vp;
	}
	
	private void findChildren(Topic topic, List<IPOJOObject> childrenList, List<UUID> expandedIds) throws DAOException {
		CommentDAO cDao = new CommentDAO(ses);
		ViewPage<Comment> children = cDao.findAllequal("topic", topic.getId().toString(), 0, 0);
		
	}
}
