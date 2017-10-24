package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@JsonRootName("еxpenditurе")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__еxpendituries", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Еxpenditurе extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "еxpendituries/" + getIdentifier();
    }
}
