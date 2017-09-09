package staff.model;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@JsonRootName("role")
@Entity
@Table(name = "staff__roles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT m FROM staff.model.Role AS m ORDER BY m.regDate"),
        @NamedQuery(name = "Role.firedRole", query = "SELECT m FROM staff.model.Role AS m WHERE m.name='fired'")
})
public class Role extends SimpleReferenceEntity {

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

    @Convert(converter = LocalizedValConverter.class)
    @Column(name = "localized_descr", columnDefinition = "jsonb")
    private Map<LanguageCode, String> localizedDescr;

    @JsonIgnore
    public List<Employee> getEmployees() {
        return employees;
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
        chunk.append(super.getFullXMLChunk(ses));
        List<Language> list = null;
        try {
            LanguageDAO lDao = new LanguageDAO(ses);
            list = lDao.findAllActivated();
        } catch (DAOException e) {
            Server.logger.exception(e);
        }
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

    @Override
    public String getShortXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append("<name>" + getLocName(ses.getLang()) + "</name>");
        return chunk.toString();
    }

    public String toString(){
        return name;
    }
}
