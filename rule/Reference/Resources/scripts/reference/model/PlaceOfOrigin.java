package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("placeoforigin")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "places_of_origin", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "PlaceOfOrigin.findAll", query = "SELECT m FROM PlaceOfOrigin AS m ORDER BY m.regDate")
public class PlaceOfOrigin extends SimpleReferenceEntity {

}
