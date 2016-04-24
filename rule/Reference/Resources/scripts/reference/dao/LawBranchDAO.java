package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.LawBranch;

public class LawBranchDAO extends ReferenceDAO<LawBranch, UUID> {

	public LawBranchDAO(_Session session) {
		super(LawBranch.class, session);
	}

}
