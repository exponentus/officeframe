package calendar.ui;

import com.exponentus.common.model.constants.PriorityType;
import com.exponentus.common.ui.filter.FilterForm;
import com.exponentus.common.ui.filter.FilterGroup;
import com.exponentus.common.ui.filter.FilterItem;
import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getEventOptions() {
        ViewPageOptions vo = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.setClassName("vw-35");
        cg.add(new ViewColumn("title"));

        ViewColumnGroup cg2 = new ViewColumnGroup();
        cg2.setClassName("vw-35");
        cg2.add(new ViewColumn("eventTime").name("event_time").type(ViewColumnType.date).format("DD.MM.YYYY").sortBoth());
        cg2.add(new ViewColumn("priority").type(ViewColumnType.translate));

        ViewColumnGroup cg3 = new ViewColumnGroup();
        cg3.setClassName("vw-30");
        cg3.add(new ViewColumn("tags").type(ViewColumnType.localizedName).style("return {color:it.color}"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);
        list.add(cg2);
        list.add(cg3);

        vo.setRoot(list);

        return vo;
    }

    public ViewPageOptions getReminderOptions() {
        ViewPageOptions vo = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.setClassName("vw-60");
        cg.add(new ViewColumn("title"));

        ViewColumnGroup cg2 = new ViewColumnGroup();
        cg2.setClassName("vw-40");
        cg2.add(new ViewColumn("reminderType").name("reminder_type").type(ViewColumnType.translate));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);
        list.add(cg2);
        vo.setRoot(list);

        return vo;
    }

    public FilterForm getEventFilter(_Session session) {
        List<FilterItem.Item> priorityTypeItems = new ArrayList<>();
        for (PriorityType priorityType : PriorityType.values()) {
            if (priorityType == PriorityType.UNKNOWN) {
                continue;
            }
            String name = Environment.vocabulary.getWord(priorityType.name().toLowerCase(), session.getLang());
            priorityTypeItems.add(new FilterItem.Item(priorityType.name(), name, "priority-" + priorityType.name().toLowerCase()));
        }

        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("priority").items(priorityTypeItems));
        filterGroup.addItem(new FilterItem("tags").url("/Reference/api/tags").style("return {color:it.color}"));

        FilterForm filterForm = new FilterForm();
        filterForm.addGroup(filterGroup);

        return filterForm;
    }
}
