package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__document_languages", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
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
    public boolean compose(_Session ses, Map<String, ?> data) {
        if (super.compose(ses, data)) {
            try {
                code = LanguageCode.valueOf((String) data.get("code"));
                return true;
            } catch (Exception e) {
                Lg.exception(e);
            }
        }
        return false;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "document-languages/" + getId();
    }
}
