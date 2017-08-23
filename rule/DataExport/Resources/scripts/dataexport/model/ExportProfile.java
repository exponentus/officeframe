package dataexport.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import dataexport.model.constants.ExportFormatType;
import dataexport.model.constants.RequestType;

import javax.persistence.*;
import java.util.Date;

@JsonRootName("exportProfile")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "de__export_profiles")
public class ExportProfile extends SimpleReferenceEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private RequestType requestType = RequestType.PARAMETERS_FORM;

	private String entityName;

	private Date from;

	private Date to;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private ExportFormatType outputFormat = ExportFormatType.UNKNOWN;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public ExportFormatType getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(ExportFormatType outputFormat) {
		this.outputFormat = outputFormat;
	}



	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

}
