package dataexport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import dataexport.model.constants.ExportFormatType;

@Entity
@Table(name = "export_profiles", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "ExportProfile.findAll", query = "SELECT m FROM ExportProfile AS m ORDER BY m.regDate")
@JsonIgnoreType
public class ExportProfile extends SimpleReferenceEntity {

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 3)
	private ExportFormatType format = ExportFormatType.UNKNOWN;

}
