package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.localization.constants.LanguageCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;

@JsonRootName("documentLanguage")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__document_languages", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
@NamedQuery(name = "DocumentLanguage.findAll", query = "SELECT m FROM DocumentLanguage AS m ORDER BY m.regDate")
public class DocumentLanguage extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 7, unique = true)
    private LanguageCode code = LanguageCode.UNKNOWN;

    public LanguageCode getCode() {
        return code;
    }

    public void setCode(LanguageCode code) {
        this.code = code;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "document-languages/" + getIdentifier();
    }
}
