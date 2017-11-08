package discussing.ui;

import com.exponentus.common.ui.filter.FilterForm;
import com.exponentus.common.ui.filter.FilterGroup;
import com.exponentus.common.ui.filter.FilterItem;
import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;
import discussing.model.constants.TopicStatusType;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getTopicOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("title"));
        cg.add(new ViewColumn("tags").type(ViewColumnType.localizedName).style("return { orgColor:it.orgColor }"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public FilterForm getTopicFilter(_Session session) {
        /*
        topic: {
            fieldsets: [{
                fields: [{
                    type: 'select',
                    name: 'status',
                    placeHolder: 'status',
                    values: {
                        items: []
                    }
                }, {
                    type: 'select',
                    name: 'tags',
                    placeHolder: 'tags',
                    values: {
                        multiple: true,
                        url: `${REFERENCE_URL.API_TAGS}?hidden=false&category=discussing`,
                        targetValue: 'id',
                        searchable: false
                    },
                    styler: (it: any) => { return { orgColor: it.orgColor }; }
                }, {
                    type: 'select',
                    name: 'author',
                    placeHolder: 'author',
                    values: {
                        url: STAFF_URL.API_EMPLOYEES,
                        targetValue: 'userID'
                    }
                }]
            }]
        }
         */
        List<FilterItem.Item> items = new ArrayList<>();
        for (TopicStatusType type : TopicStatusType.values()) {
            if (type == TopicStatusType.UNKNOWN) {
                continue;
            }

            String name = Environment.vocabulary.getWord(type.name().toLowerCase(), session.getLang());
            items.add(new FilterItem.Item(type.name(), name, "status-" + type.name().toLowerCase()));
        }

        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("status").items(items));
        filterGroup.addItem(new FilterItem("tags").multiple().url("/Reference/api/tags?hidden=false&category=discussing").style("return {orgColor:it.orgColor}"));
        filterGroup.addItem(new FilterItem("author").targetValue("userID").url("/Staff/api/employees"));

        filterForm.addGroup(filterGroup);

        return filterForm;
    }
}
