package dataexport.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.exponentus.localization.constants.LanguageCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import dataexport.init.ModuleConst;
import dataexport.model.constants.ExportFormatType;
import dataexport.model.constants.ReportQueryType;
import reference.model.Tag;
import staff.model.embedded.Observer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = ModuleConst.CODE + "__report_profiles")
public class ReportProfile extends SimpleReferenceEntity {

    @Convert(converter = LocalizedValConverter.class)
    @Column(name = "localized_descr", columnDefinition = "jsonb")
    private Map<LanguageCode, String> localizedDescr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ReportQueryType reportQueryType = ReportQueryType.ENTITY_REQUEST;

    @Column(name = "class_name")
    private String className;

    @Column(name = "start_form")
    private Date startFrom;

    @Column(name = "end_until")
    private Date endUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 4)
    private ExportFormatType outputFormat = ExportFormatType.UNKNOWN;

    @ElementCollection
    @CollectionTable(name = "de__report_profile_observers", joinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Observer> observers = new ArrayList<Observer>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "de__report_profile_tags")
    private List<Tag> tags;

    public Map<LanguageCode, String> getLocalizedDescr() {
        return localizedDescr;
    }

    public void setLocalizedDescr(Map<LanguageCode, String> localizedDescr) {
        this.localizedDescr = localizedDescr;
    }

    public ReportQueryType getReportQueryType() {
        return reportQueryType;
    }

    public void setReportQueryType(ReportQueryType reportQueryType) {
        this.reportQueryType = reportQueryType;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "report-profiles/" + getIdentifier();
    }
}
