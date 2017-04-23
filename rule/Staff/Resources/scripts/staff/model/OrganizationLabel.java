package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.util.List;

@JsonRootName("organizationLabel")
@Entity
@Table(name = "staff__org_labels", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "OrganizationLabel.findAll", query = "SELECT m FROM OrganizationLabel AS m ORDER BY m.regDate")
public class OrganizationLabel extends SimpleReferenceEntity {

    @ManyToMany(mappedBy = "labels")
    private List<Organization> labels;

    @JsonIgnore
    public List<Organization> getLabels() {
        return labels;
    }

    @Override
    public String getShortXMLChunk(_Session ses) {
        return "<name>" + getLocName(ses.getLang()) + "</name>";
    }

    @Override
    public String getURL() {
        return "p?id=organization-label-form&amp;docid=" + getId();
    }
}
