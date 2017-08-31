package dataexport.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import dataexport.init.AppConst;
import dataexport.model.constants.ExportFormatType;
import dataexport.model.constants.ReportQueryType;
import reference.model.Tag;
import staff.model.embedded.Observer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonRootName("reportProfile")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "de__report_profiles")
public class ReportProfile extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ReportQueryType reportQueryType = ReportQueryType.ENTITY_REQUEST;

    private String entityName;

    @Column(name = "start_form")
    private Date startFrom;

    @Column(name = "end_untill")
    private Date endUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private ExportFormatType outputFormat = ExportFormatType.UNKNOWN;

    @ElementCollection
    @CollectionTable(name = "de__report_profile_observers", joinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Observer> observers = new ArrayList<Observer>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "de__report_profile_tags")
    private List<Tag> tags;

    private String description;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Date getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(Date startFrom) {
        this.startFrom = startFrom;
    }

    public Date getEndUntil() {
        return endUntil;
    }

    public void setEndUntil(Date endUntil) {
        this.endUntil = endUntil;
    }

    public ExportFormatType getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(ExportFormatType outputFormat) {
        this.outputFormat = outputFormat;
    }

    public ReportQueryType getReportQueryType() {
        return reportQueryType;
    }

    public void setReportQueryType(ReportQueryType reportQueryType) {
        this.reportQueryType = reportQueryType;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "report-profiles/" + getIdentifier();
    }
}
