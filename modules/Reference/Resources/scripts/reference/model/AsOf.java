package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = ModuleConst.CODE + "__asof", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class AsOf extends SimpleReferenceEntity {

    @Column(name = "asof_by_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date asOfByDate;

    @Column(name = "allowed_to_publish")
    private boolean isAllowedToPublish;

    public Date getAsOfByDate() {
        return asOfByDate;
    }

    public void setAsOfByDate(Date asOfByDate) {
        this.asOfByDate = asOfByDate;
    }

    public boolean isAllowedToPublish() {
        return isAllowedToPublish;
    }

    public void setAllowedToPublish(boolean allowedToPublish) {
        isAllowedToPublish = allowedToPublish;
    }

    @Override
    public boolean compose(_Session ses, Map<String, ?> data) {
        if (super.compose(ses, data)) {

            try {
                asOfByDate = TimeUtil.stringToDateWithPassion((String) data.get("asOfByDate"));
            } catch (Exception e) {
                Lg.exception(e);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "as-of/" + getId();
    }
}
