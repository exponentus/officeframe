package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("lawarticle")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__law_articles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "LawArticle.findAll", query = "SELECT m FROM LawArticle AS m ORDER BY m.regDate")
public class LawArticle extends SimpleReferenceEntity {
}
