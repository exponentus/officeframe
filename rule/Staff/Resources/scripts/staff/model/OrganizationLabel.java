package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.exponentus.localization.constants.LanguageCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import staff.init.AppConst;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@JsonRootName("organizationLabel")
@Entity
@Table(name = AppConst.CODE + "__org_labels", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "OrganizationLabel.findAll", query = "SELECT m FROM OrganizationLabel AS m ORDER BY m.regDate")
public class OrganizationLabel extends SimpleReferenceEntity {

    @ManyToMany(mappedBy = "labels")
    private List<Organization> labels;

    @Convert(converter = LocalizedValConverter.class)
    @Column(name = "localized_descr", columnDefinition = "jsonb")
    private Map<LanguageCode, String> localizedDescr;

    @JsonIgnore
    public List<Organization> getLabels() {
        return labels;
    }

    public Map<LanguageCode, String> getLocalizedDescr() {
        return localizedDescr;
    }

    public void setLocalizedDescr(Map<LanguageCode, String> localizedDescr) {
        this.localizedDescr = localizedDescr;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "organization-labels/" + getIdentifier();
    }
}
