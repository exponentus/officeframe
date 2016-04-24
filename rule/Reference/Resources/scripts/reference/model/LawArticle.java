package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleEntity;

@Entity
@Table(name = "law_articles", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "LawArticle.findAll", query = "SELECT m FROM LawArticle AS m ORDER BY m.regDate")
public class LawArticle extends SimpleEntity {

}
