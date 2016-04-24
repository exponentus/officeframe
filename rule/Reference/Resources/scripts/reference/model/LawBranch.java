package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleEntity;

@Entity
@Table(name = "law_branches", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "LawBranch.findAll", query = "SELECT m FROM LawBranch AS m ORDER BY m.regDate")
public class LawBranch extends SimpleEntity {

}
