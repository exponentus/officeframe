package reference.model;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.model.constants.ApprovalSchemaType;
import reference.model.constants.converter.ApprovalSchemaTypeConverter;
import reference.model.embedded.RouteBlock;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@JsonRootName("approvalRoute")
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
    @Column(name = "localized_descr", columnDefinition = "json")
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
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append("<regdate>" + TimeUtil.dateTimeToStringSilently(regDate) + "</regdate>");
        chunk.append("<name>" + name + "</name>");
        chunk.append("<ison>" + isOn + "</ison>");
        chunk.append("<schema>" + schema + "</schema>");
        chunk.append("<category>" + category + "</category>");

        chunk.append("<routeblocks>");
        if (routeBlocks != null) {
            for (RouteBlock b : routeBlocks) {
                chunk.append("<entry id=\"" + b.getIdentifier() + "\">" + b.getFullXMLChunk(ses) + "</entry>");
            }
        }
        chunk.append("</routeblocks>");

        List<Language> list = null;
        try {
            LanguageDAO lDao = new LanguageDAO(ses);
            list = lDao.findAllActivated();
        } catch (DAOException e) {
            Server.logger.exception(e);
        }

        chunk.append("<localizednames>");
        if (list != null) {
            for (Language l : list) {
                chunk.append("<entry id=\"" + l.getCode() + "\">" + getLocName(l.getCode()) + "</entry>");
            }
        }
        chunk.append("</localizednames>");

        chunk.append("<localizeddescr>");
        if (list != null) {
            for (Language l : list) {
                try {
                    chunk.append("<entry id=\"" + l.getCode() + "\">" + localizedDescr.get(l.getCode()) + "</entry>");
                } catch (NullPointerException e) {
                    chunk.append("<entry id=\"" + l.getCode() + "\"></entry>");
                }
            }
        }
        chunk.append("</localizeddescr>");
        return chunk.toString();
    }
}
