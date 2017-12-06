package monitoring.dao;

import com.exponentus.common.dao.SimpleDAO;
import com.exponentus.scripting._Session;
import monitoring.model.DocumentActivity;

public class DocumentActivityDAO extends SimpleDAO<DocumentActivity> {

    public DocumentActivityDAO(_Session ses) {
        super(DocumentActivity.class);
    }

}
