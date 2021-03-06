package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

/**
 * Сущность Право пользования
 *
 * @author Kayra created 07-01-2016
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__property_codes", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "PropertyCode.findAll", query = "SELECT m FROM PropertyCode AS m ORDER BY m.regDate")
public class PropertyCode extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "property-codes/" + getId();
    }
}
