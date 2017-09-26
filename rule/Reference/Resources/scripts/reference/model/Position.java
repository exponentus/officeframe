package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;

/**
 * @author Kayra created 07-01-2016
 */

@JsonRootName("position")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__positions", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
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
        return AppConst.BASE_URL + "positions/" + getIdentifier();
    }
}
