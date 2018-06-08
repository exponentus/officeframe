package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import reference.init.ModuleConst;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = ModuleConst.CODE + "__statistic__indicator_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class StatisticIndicatorType extends SimpleReferenceEntity {

    @JoinColumn(name = "statistic_type_id", nullable = false)
    private StatisticType statisticType;

    @JoinColumn(name = "unit_type_id", nullable = false)
    private UnitType unitType;

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "statistic-indicator-types/" + getIdentifier();
    }
}
