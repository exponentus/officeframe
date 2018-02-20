package monitoring.ui;

import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewOption;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewOption getUserActivityOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.setClassName("vw-30");
        cg.add(new ViewColumn("eventTime").name("event_time"));
        cg.add(new ViewColumn("ip"));

        ViewColumnGroup cg2 = new ViewColumnGroup();
        cg2.setClassName("vw-35");
        cg2.add(new ViewColumn("country"));

        ViewColumnGroup cg3 = new ViewColumnGroup();
        cg3.setClassName("vw-35");
        cg3.add(new ViewColumn("type"));
        cg3.add(new ViewColumn("actUser.login").name("act_user"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);
        list.add(cg2);
        list.add(cg3);

        result.setRoot(list);
        return result;
    }

    public ViewOption getLastVisitOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.setClassName("vw-50");
        cg.add(new ViewColumn("actUser.login").name("act_user"));
        cg.add(new ViewColumn("eventTime").name("event_time"));

        ViewColumnGroup cg2 = new ViewColumnGroup();
        cg2.setClassName("vw-50");
        cg2.add(new ViewColumn("ip"));
        cg2.add(new ViewColumn("country"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);
        list.add(cg2);

        result.setRoot(list);
        return result;
    }

    public ViewOption getCountOfRecordOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("actUser").name("act_user"));
        cg.add(new ViewColumn("count"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }
}
