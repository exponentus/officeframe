package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.util.List;

@JsonRootName("role")
@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "Role.findAll", query = "SELECT m FROM Role AS m ORDER BY m.regDate")
public class Role extends SimpleReferenceEntity {

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

    @JsonIgnore
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        return chunk.toString();
    }

    @Override
    public String getShortXMLChunk(_Session ses) {
        return "<name>" + getLocName(ses.getLang()) + "</name>";
    }
}
