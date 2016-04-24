package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.LawArticle;

public class LawArticleDAO extends ReferenceDAO<LawArticle, UUID> {

	public LawArticleDAO(_Session session) {
		super(LawArticle.class, session);
	}

}
