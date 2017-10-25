package dataexport.ui;

import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getReportProfileOptions() {
        /*
         'report-profiles': [{
            columns: [
                { name: 'name', value: 'name', type: 'localizedName' },
                { name: 'class_name', value: 'className' },
                { name: 'report_query_type', value: 'reportQueryType' },
                { name: 'output_format', value: 'outputFormat' },
                { name: 'start_from', value: 'startFrom', type: 'date', format: 'DD.MM.YYYY' },
                { name: 'end_until', value: 'endUntil', type: 'date', format: 'DD.MM.YYYY' }
            ]
        }]
         */

        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.setClassName("vw-45");
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("outputFormat").name("output_format"));

        ViewColumnGroup cg2 = new ViewColumnGroup();
        cg2.setClassName("vw-55");
        cg2.add(new ViewColumn("startFrom").name("start_from").type(ViewColumnType.date).format("DD.MM.YYYY"));
        cg2.add(new ViewColumn("endUntil").name("end_until").type(ViewColumnType.date).format("DD.MM.YYYY"));
        cg2.add(new ViewColumn("tags").type(ViewColumnType.localizedName).className("vw-tags").style("return { color: it.color }"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);
        list.add(cg2);

        result.addOption("root", list);
        return result;
    }
}
