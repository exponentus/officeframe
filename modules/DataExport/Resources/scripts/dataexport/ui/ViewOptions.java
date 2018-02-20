package dataexport.ui;

import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewOption;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewOption getReportProfileOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.setClassName("vw-45");
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("outputFormat").name("output_format").className("vw-25"));

        ViewColumnGroup cg2 = new ViewColumnGroup();
        cg2.setClassName("vw-55");
        cg2.add(new ViewColumn("startFrom").name("start_from").type(ViewColumnType.date).format("DD.MM.YYYY"));
        cg2.add(new ViewColumn("endUntil").name("end_until").type(ViewColumnType.date).format("DD.MM.YYYY"));
        cg2.add(new ViewColumn("tags").type(ViewColumnType.localizedName).style("return { color: it.color }"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);
        list.add(cg2);

        result.setRoot(list);
        return result;
    }
}
