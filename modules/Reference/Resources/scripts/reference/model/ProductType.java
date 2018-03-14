package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = ModuleConst.CODE + "__product_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ProductType extends SimpleReferenceEntity {


}
