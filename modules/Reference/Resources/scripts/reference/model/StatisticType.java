package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import reference.init.ModuleConst;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = ModuleConst.CODE + "__statistic__type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class StatisticType extends SimpleReferenceEntity {

    @JsonIgnore
    @JoinColumn(name = "statistic_type_id")
    private StatisticType parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("name ASC")
    private List<StatisticType> children;

    @JsonIgnore
    @OneToMany(mappedBy = "statisticType", cascade = CascadeType.ALL)
    private List<StatisticIndicatorType> indicatorTypes;

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "statistic-types/" + getIdentifier();
    }
}
