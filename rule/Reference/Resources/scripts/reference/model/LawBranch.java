package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("lawBranch")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__law_branches", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "LawBranch.findAll", query = "SELECT m FROM LawBranch AS m ORDER BY m.regDate")
public class LawBranch extends SimpleReferenceEntity {
}
