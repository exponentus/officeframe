package reference.model.embedded;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import reference.model.constants.ApprovalType;
import reference.model.constants.converter.ApprovalTypeConverter;
import staff.model.Employee;

@Entity
@Table(name = "route_blocks")
@JsonPropertyOrder({ "id", "name" })
public class RouteBlock extends SimpleAppEntity implements IPOJOObject {

	private int position;

	private List<Employee> approvers;

	@Convert(converter = ApprovalTypeConverter.class)
	private ApprovalType type = ApprovalType.UNKNOWN;

	@Column(name = "time_limit")
	private int timeLimit;

	@Column(name = "require_comment_if_no")
	private boolean requireCommentIfNo;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Employee> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<Employee> approvers) {
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
		chunk.append("<position>" + position + "</position>");
		chunk.append("<type>" + type + "</type>");
		chunk.append("<timelimit>" + timeLimit + "</timelimit>");
		chunk.append("<requirecommentifno>" + requireCommentIfNo + "</requirecommentifno>");
		chunk.append("<approvers>");

		for (Employee b : approvers) {
			chunk.append("<entry id=\"" + b.getId() + "\">" + b.getFullXMLChunk(ses) + "</entry>");
		}

		chunk.append("</approvers>");
		return chunk.toString();
	}

}
