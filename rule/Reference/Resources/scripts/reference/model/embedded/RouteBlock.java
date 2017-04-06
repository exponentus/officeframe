package reference.model.embedded;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import administrator.model.User;
import reference.model.ApprovalRoute;
import reference.model.constants.ApprovalType;
import reference.model.constants.converter.ApprovalTypeConverter;

@Entity
@Table(name = "route_blocks")
@JsonPropertyOrder({ "id", "name" })
public class RouteBlock extends SimpleAppEntity implements IPOJOObject {

	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private ApprovalRoute route;

	private List<User> approvers;

	@Convert(converter = ApprovalTypeConverter.class)
	private ApprovalType type = ApprovalType.UNKNOWN;

	@Column(name = "time_limit")
	private int timeLimit;

	@Column(name = "require_comment_if_no")
	private boolean requireCommentIfNo;

	public ApprovalRoute getRoute() {
		return route;
	}

	public void setRoute(ApprovalRoute route) {
		this.route = route;
	}

	public List<User> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<User> approvers) {
		this.approvers = approvers;
	}

	public ApprovalType getType() {
		return type;
	}

	public void setType(ApprovalType type) {
		this.type = type;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public boolean isRequireCommentIfNo() {
		return requireCommentIfNo;
	}

	public void setRequireCommentIfNo(boolean requireCommentIfNo) {
		this.requireCommentIfNo = requireCommentIfNo;
	}

	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<type>" + type + "</type>");
		chunk.append("<timelimit>" + timeLimit + "</timelimit>");
		chunk.append("<requirecommentifno>" + requireCommentIfNo + "</requirecommentifno>");
		chunk.append("<approvers>");

		for (User b : approvers) {
			chunk.append("<entry id=\"" + b.getIdentifier() + "\">" + b.getFullXMLChunk(ses) + "</entry>");
		}

		chunk.append("</approvers>");
		return chunk.toString();
	}

}
