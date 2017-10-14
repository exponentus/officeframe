package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@JsonRootName("cashInflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__cash_inflows", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class CashInflow extends SimpleReferenceEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CashInflowCategory cashInflowCategory;

    public CashInflowCategory getCashInflowCategory() {
        return cashInflowCategory;
    }

    public void setCashInflowCategory(CashInflowCategory cashInflowCategory) {
        this.cashInflowCategory = cashInflowCategory;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "tax-incomes/" + getIdentifier();
    }
}
