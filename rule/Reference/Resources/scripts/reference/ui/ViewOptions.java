package reference.ui;

import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getDistrictOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("region").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getRegionOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("type").type(ViewColumnType.localizedName));
        cg.add(new ViewColumn("country").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getDocumentTypeOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getDocumentSubjectOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getControlTypeOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("name").name(""));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getApprovalRouteOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("schema").sortBoth());
        cg.add(new ViewColumn("on").name("is_on"));
        cg.add(new ViewColumn("category").sortBoth());

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getTagOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth().style("return { orgColor:it.orgColor }"));
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }
}
