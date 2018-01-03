package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;
import reference.model.constants.UnitCategory;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__unit_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "UnitType.findAll", query = "SELECT m FROM UnitType AS m ORDER BY m.regDate")
public class UnitType extends SimpleReferenceEntity {

    @FTSearchable
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private UnitCategory category;

    public UnitCategory getCategory() {
        return category;
    }

    public void setCategory(UnitCategory category) {
        this.category = category;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "unit-types/" + getId();
    }
}
