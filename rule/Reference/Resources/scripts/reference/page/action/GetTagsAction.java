package reference.page.action;

import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import reference.dao.TagDAO;
import reference.model.Tag;

import java.util.List;


public class GetTagsAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        TagDAO dao = new TagDAO(ses);
        List<Tag> list = dao.findAll();
        addContent(new _POJOListWrapper(list, ses));
    }
}
