package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.ModuleConst;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonRootName("nationality")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__nationalities", uniqueConstraints = @UniqueConstraint(columnNames = {"loc_name"}))
@Cacheable(true)
public class Nationality extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "nationalities/" + getIdentifier();
    }
}
