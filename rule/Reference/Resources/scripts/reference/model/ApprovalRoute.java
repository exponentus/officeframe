package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.constants.ApprovalSchemaType;
import com.exponentus.common.model.constants.converter.ApprovalSchemaTypeConverter;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.exponentus.localization.constants.LanguageCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;
import reference.model.embedded.RouteBlock;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__approval_routes", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "ApprovalRoute.findAll", query = "SELECT m FROM ApprovalRoute AS m ORDER BY m.regDate")
public class ApprovalRoute extends SimpleReferenceEntity {

    @Column(name = "is_on")
    private boolean isOn;

    @Convert(converter = ApprovalSchemaTypeConverter.class)
    private ApprovalSchemaType schema;

    @Column(name = "ver_support")
    private boolean versionsSupport;

    private String category;

    @Convert(converter = LocalizedValConverter.class)
    @Column(name = "localized_descr", columnDefinition = "jsonb")
    private Map<LanguageCode, String> localizedDescr;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RouteBlock> routeBlocks;

    public ApprovalSchemaType getSchema() {
        return schema;
    }

    public void setSchema(ApprovalSchemaType schema) {
        this.schema = schema;
    }

    public boolean isVersionsSupport() {
        return versionsSupport;
    }

    public void setVersionsSupport(boolean versionsSupport) {
        this.versionsSupport = versionsSupport;
    }

    public List<RouteBlock> getRouteBlocks() {
        return routeBlocks;
    }

    public void setRouteBlocks(List<RouteBlock> routeBlocks) {
        this.routeBlocks = routeBlocks;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean isOn) {
        this.isOn = isOn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<LanguageCode, String> getLocalizedDescr() {
        return localizedDescr;
    }

    public void setLocalizedDescr(Map<LanguageCode, String> localizedDescr) {
        this.localizedDescr = localizedDescr;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "approval-routes/" + getId();
    }
}
