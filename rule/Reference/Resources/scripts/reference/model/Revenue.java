package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@JsonRootName("revenue")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__revenues", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Revenue extends SimpleReferenceEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RevenueCategory revenueCategory;

    public RevenueCategory getRevenueCategory() {
        return revenueCategory;
    }

    public void setRevenueCategory(RevenueCategory revenueCategory) {
        this.revenueCategory = revenueCategory;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "revenues/" + getIdentifier();
    }
}
