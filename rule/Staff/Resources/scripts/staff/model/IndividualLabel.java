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

@JsonRootName("individualLabel")
@Entity
@Table(name = "staff__individual_labels", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class IndividualLabel extends SimpleReferenceEntity {

    @ManyToMany(mappedBy = "labels")
    private List<Individual> individuals;

    @Convert(converter = LocalizedValConverter.class)
    @Column(name = "localized_descr", columnDefinition = "jsonb")
    private Map<LanguageCode, String> localizedDescr;

    @JsonIgnore
    public List<Individual> getIndividuals() {
        return individuals;
    }

    public Map<LanguageCode, String> getLocalizedDescr() {
        return localizedDescr;
    }

    public void setLocalizedDescr(Map<LanguageCode, String> localizedDescr) {
        this.localizedDescr = localizedDescr;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "individual-labels/" + getId();
    }
}
