package dataexport.ui;

import com.exponentus.common.ui.view.ColumnOption;
import com.exponentus.common.ui.view.ColumnOptionGroup;
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
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.add(new ColumnOption("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ColumnOption("className").name("class_name"));
        cg.add(new ColumnOption("reportQueryType").name("report_query_type"));
        cg.add(new ColumnOption("outputFormat").name("output_format"));
        cg.add(new ColumnOption("startFrom").name("start_from").type(ViewColumnType.date).format("DD.MM.YYYY"));
        cg.add(new ColumnOption("endUntil").name("end_until").type(ViewColumnType.date).format("DD.MM.YYYY"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }
}
