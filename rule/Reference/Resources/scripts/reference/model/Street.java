package reference.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.dao.LocalityDAO;
import reference.dao.RegionDAO;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonRootName("street")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__streets", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "locality_id"}))
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
    public boolean compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);
        try {
            Map<String,String> localityMap = (Map<String,String>)data.get("locality");
            CollationDAO collationDAO = new CollationDAO(ses);
            Collation collation = collationDAO.findByExtKey((String)localityMap.get("id"));
            LocalityDAO localityDAO = new LocalityDAO(ses);
            Locality locality = localityDAO.findById(collation.getIntKey());
            this.locality = locality;

            streetId = (Integer) data.get("streetId");

        }catch (Exception e){
            Lg.exception(e);
            return false;
        }
        return true;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "streets/" + getId();
    }
}
