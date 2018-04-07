package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;
import reference.model.constants.LocalityCode;

import javax.persistence.*;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__locality_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
@NamedQuery(name = "LocalityType.findAll", query = "SELECT m FROM LocalityType AS m ORDER BY m.regDate")
public class LocalityType extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 32)
    private LocalityCode code = LocalityCode.UNKNOWN;

    public LocalityCode getCode() {
        return code;
    }

    public void setCode(LocalityCode code) {
        this.code = code;
    }

    @Override
    public boolean compose(_Session ses, Map<String, ?> data) {
        if (super.compose(ses, data)) {

            try {
                code = LocalityCode.valueOf((String) data.get("code"));
            } catch (Exception e) {
                Lg.exception(e);
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "locality-types/" + getId();
    }
}
