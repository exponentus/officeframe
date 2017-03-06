package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

/**
 * @author Kayra created 11-11-2016
 */

@JsonRootName("demandtype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "demand_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "DemandType.findAll", query = "SELECT m FROM DemandType AS m ORDER BY m.regDate")
public class DemandType extends SimpleReferenceEntity {
    public String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        if (this.prefix != null) {
            chunk.append("<prefix>" + this.prefix + "</prefix>");
        }
        return chunk.toString();
    }
}
