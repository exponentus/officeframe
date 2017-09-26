package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonRootName("street")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__streets", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "locality_id"}))
@NamedQuery(name = "Street.findAll", query = "SELECT m FROM Street AS m ORDER BY m.regDate")
public class Street extends SimpleReferenceEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Locality locality;

    @Column(name = "street_id")
    private int streetId;

    public int getStreetId() {
        return streetId;
    }

    public void setStreetId(int streetId) {
        this.streetId = streetId;
    }

    public Locality getLocality() {
        return locality;
    }

    @JsonProperty
    public void setLocality(Locality city) {
        this.locality = city;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "streets/" + getIdentifier();
    }
}
