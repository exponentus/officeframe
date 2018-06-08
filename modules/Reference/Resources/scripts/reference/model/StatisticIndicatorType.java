package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import reference.init.ModuleConst;

import javax.persistence.*;

@Entity
@Table(name = ModuleConst.CODE + "__statistic__indicator_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class StatisticIndicatorType extends SimpleReferenceEntity {

    @Column(length = 16, unique = true)
    private String code;

    @JoinColumn(name = "statistic_type_id", nullable = false)
    private StatisticType statisticType;

    @JoinColumn(name = "unit_type_id", nullable = false)
    private UnitType unitType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public StatisticType getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(StatisticType statisticType) {
        this.statisticType = statisticType;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "statistic-indicator-types/" + getIdentifier();
    }
}
