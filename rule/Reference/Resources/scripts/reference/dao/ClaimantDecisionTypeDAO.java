package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.ClaimantDecisionType;

public class ClaimantDecisionTypeDAO extends ReferenceDAO<ClaimantDecisionType, UUID> {

	public ClaimantDecisionTypeDAO(_Session session) {
		super(ClaimantDecisionType.class, session);
	}

}
