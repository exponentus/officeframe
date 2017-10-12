package monitoring.ui;

import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewPageOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getUserActivityOptions() {
        /*
        'user-activities': [{
            columns: [
                { name: 'event_time', value: 'eventTime' },
                { name: 'ip', value: 'ip' },
                { name: 'country', value: 'country' },
                { name: 'type', value: 'type' },
                { name: 'act_user', value: 'actUser.login' }
            ]
        }] */

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("eventTime").name("event_time"));
        cg.add(new ViewColumn("ip"));
        cg.add(new ViewColumn("country"));
        cg.add(new ViewColumn("type"));
        cg.add(new ViewColumn("actUser.login").name("act_user"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getLastVisitOptions() {
        /*
    'last-visits': [{
        columns: [
            { name: 'act_user', value: 'actUser.login' },
            { name: 'event_time', value: 'eventTime' },
            { name: 'ip', value: 'ip' },
            { name: 'country', value: 'country' }
        ]
    }]*/

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("actUser.login").name("act_user"));
        cg.add(new ViewColumn("eventTime").name("event_time"));
        cg.add(new ViewColumn("ip"));
        cg.add(new ViewColumn("country"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getCountOfRecordOptions() {
        /*
    'count-of-records': [{
        columns: [
            { name: 'act_user', value: 'actUser' },
            { name: 'count', value: 'count' }
        ]
    }]*/

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("actUser").name("act_user"));
        cg.add(new ViewColumn("count"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }
}
