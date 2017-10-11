package monitoring.ui;

import com.exponentus.common.ui.view.ColumnOption;
import com.exponentus.common.ui.view.ColumnOptionGroup;
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
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.add(new ColumnOption("eventTime").name("event_time"));
        cg.add(new ColumnOption("ip"));
        cg.add(new ColumnOption("country"));
        cg.add(new ColumnOption("type"));
        cg.add(new ColumnOption("actUser.login").name("act_user"));

        List<ColumnOptionGroup> list = new ArrayList<>();
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
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.add(new ColumnOption("actUser.login").name("act_user"));
        cg.add(new ColumnOption("eventTime").name("event_time"));
        cg.add(new ColumnOption("ip"));
        cg.add(new ColumnOption("country"));

        List<ColumnOptionGroup> list = new ArrayList<>();
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
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.add(new ColumnOption("actUser").name("act_user"));
        cg.add(new ColumnOption("count"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }
}
