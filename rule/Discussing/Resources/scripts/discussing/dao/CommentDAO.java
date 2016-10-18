package discussing.dao;

import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;
import discussing.model.Comment;

import java.util.UUID;

public class CommentDAO extends DAO<Comment, UUID> {

	public CommentDAO(_Session session) {
		super(Comment.class, session);
	}

}
