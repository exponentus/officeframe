package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@JsonRootName("taxIncome")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__tax_incomes", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class TaxIncome extends SimpleReferenceEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private TaxIncomeCategory taxIncomeCategory;

    public TaxIncomeCategory getTaxIncomeCategory() {
        return taxIncomeCategory;
    }

    public void setTaxIncomeCategory(TaxIncomeCategory taxIncomeCategory) {
        this.taxIncomeCategory = taxIncomeCategory;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "tax-incomes/" + getIdentifier();
    }
}
