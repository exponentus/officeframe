package discussing.dao;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;

import discussing.model.Comment;
import discussing.model.Topic;

public class TopicDAO extends DAO<Topic, UUID> {

	public TopicDAO(_Session session) {
		super(Topic.class, session);
	}

	public ViewPage<Topic> findAllWithChildren(int pageNum, int pageSize, List<UUID> expandedIdList) {
		List<Topic> list = findAll(pageNum, pageSize);

		List<IPOJOObject> childrenList = null; // findTaskStream(expandedIds);

		for (Topic task : list) {
			if (task.isHasComments() && Collections.disjoint(task.getComments(), expandedIdList)) {

			}
		}

		ViewPage<Topic> vp = new ViewPage(list);

		return vp;
	}

	private void findChildren(Topic topic, List<IPOJOObject> childrenList, List<UUID> expandedIds) {
		CommentDAO cDao = new CommentDAO(ses);
		ViewPage<Comment> children = cDao.findAllequal("topic", topic.getId().toString(), 0, 0);

	}
}
