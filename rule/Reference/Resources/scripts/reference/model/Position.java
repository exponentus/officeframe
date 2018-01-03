package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__positions", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "Position.findAll", query = "SELECT m FROM Position AS m ORDER BY m.regDate")
public class Position extends SimpleReferenceEntity {
    private int rank = 999;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "positions/" + getIdentifier();
    }
}
