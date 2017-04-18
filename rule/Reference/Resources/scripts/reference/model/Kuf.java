package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.model.constants.KufType;

import javax.persistence.*;

@JsonRootName("kuf")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__kufs")
@Cacheable(true)
@NamedQuery(name = "Kuf.findAll", query = "SELECT m FROM Kuf AS m ORDER BY m.regDate")
public class Kuf extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 32, unique = true)
    private KufType code = KufType.UNKNOWN;

    public KufType getKuf() {
        return code;
    }

    public void setKuf(KufType kuf) {
        this.code = kuf;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        chunk.append("<kufcode>" + code + "</kufcode>");
        return chunk.toString();
    }
}
