package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "___expenditures", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Expenditure extends SimpleReferenceEntity {

    @NotNull
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private ExpenditureCategory category;


    public ExpenditureCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenditureCategory category) {
        this.category = category;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "expenditures/" + getId();
    }
}
