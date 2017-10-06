package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonRootName("realEstateObjPurpose")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = AppConst.CODE + "__realestate_purposes", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class RealEstateObjPurpose extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "realesate-purposes/" + getIdentifier();
    }
}
