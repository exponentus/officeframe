package discussing.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.runtimeobj.IAppEntity;
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
		ViewPage<Topic> vp = new ViewPage(list);

		if (vp.getResult().isEmpty()/* || expandedIds.isEmpty() */) {
			return vp;
		}

		List<IPOJOObject> childrenList = null; // findTaskStream(expandedIds);

		for (Topic task : vp.getResult()) {
			// findChildren(task, childrenList, expandedIdList);
		}
		return vp;
	}

	private void findChildren(Comment comment, List<IPOJOObject> childrenList, List<UUID> expandedIds) {
		if (comment.isHasSubComments()) {
			List<IAppEntity> children = new ArrayList<>(comment.getSubComments());

			Supplier<List<IAppEntity>> supplier = LinkedList::new;
			children = children.stream().sorted((m1, m2) -> m1.getRegDate().after(m2.getRegDate()) ? 1 : -1)
			        .collect(Collectors.toCollection(supplier));

			// comment.setSubComments(children);

			for (Comment t : comment.getSubComments()) {
				findChildren(t, childrenList, expandedIds);
			}
		}
	}
}
