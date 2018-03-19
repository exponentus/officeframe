package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__operativeinfotypes", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class OperativeInfoType extends SimpleReferenceEntity {
    private int rank = 999;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "operative-info-types/" + getId();
    }
}
