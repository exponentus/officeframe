package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonRootName("street")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "streets", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "locality_id"}))
@NamedQuery(name = "Street.findAll", query = "SELECT m FROM Street AS m ORDER BY m.regDate")
public class Street extends SimpleReferenceEntity {
    @JsonIgnore
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

    public void setLocality(Locality city) {
        this.locality = city;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        chunk.append("<streetid>" + streetId + "</streetid>");
        if (locality != null) {
            chunk.append(
                    "<locality id=\"" + locality.getId() + "\">" + locality.getLocName(ses.getLang()) + "</locality>");
        }
        return chunk.toString();
    }
}
