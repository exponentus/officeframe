package reference.dao.filter;

import com.exponentus.dataengine.IFilter;
import com.exponentus.scripting.WebFormData;
import reference.model.StatisticIndicatorType;
import reference.model.StatisticType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class StatisticIndicatorTypeFilter implements IFilter<StatisticIndicatorType> {

    private StatisticType statisticType;

    public StatisticIndicatorTypeFilter(WebFormData params) {
        String typeId = params.getValueSilently("statisticType");
        if (!typeId.isEmpty()) {
            statisticType = new StatisticType();
            statisticType.setId(UUID.fromString(typeId));
        }
    }

    public StatisticType getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(StatisticType statisticType) {
        this.statisticType = statisticType;
    }

    @Override
    public Predicate collectPredicate(Root<StatisticIndicatorType> root, CriteriaBuilder cb, Predicate condition) {

        if (statisticType != null) {
            if (condition == null) {
                condition = cb.equal(root.get("statisticType"), statisticType);
            } else {
                condition = cb.and(cb.equal(root.get("statisticType"), statisticType), condition);
            }
        }

        return condition;
    }
}
