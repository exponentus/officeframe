package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import java.util.Map;

@JsonRootName("documentLanguage")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__document_languages", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
@NamedQuery(name = "DocumentLanguage.findAll", query = "SELECT m FROM DocumentLanguage AS m ORDER BY m.regDate")
public class DocumentLanguage extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7, unique = true)
    private LanguageCode code = LanguageCode.UNKNOWN;

    public LanguageCode getCode() {
        return code;
    }

    public void setCode(LanguageCode code) {
        this.code = code;
    }

    @Override
    public DocumentLanguage compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);

        try {
            code = LanguageCode.valueOf((String) data.get("code"));
        } catch (Exception e) {
            Lg.exception(e);
            return null;
        }
        return this;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "document-languages/" + getId();
    }
}
