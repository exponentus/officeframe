package discussing.dao;

import java.util.UUID;

import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;

import discussing.model.Comment;

public class CommentDAO extends DAO<Comment, UUID> {

	public CommentDAO(_Session session) {
		super(Comment.class, session);
	}

}
