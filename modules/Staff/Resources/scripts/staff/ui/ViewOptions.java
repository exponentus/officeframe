package staff.ui;

import com.exponentus.common.ui.filter.FilterForm;
import com.exponentus.common.ui.filter.FilterGroup;
import com.exponentus.common.ui.filter.FilterItem;
import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewOption;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewOption getOrgOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("orgCategory").name("org_category").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getDepOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("organization").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getEmpOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.setClassName("vw-40 vw-sm-100");
        cg.add(new ViewColumn("avatarURLSm").name("").type(ViewColumnType.image).cellStyle("return { 'max-width': '50px', 'width': '50px' }"));
        cg.add(new ViewColumn("name").sortBoth().style("return it.fired ? { 'color': '#777', 'text-decoration': 'line-through' } : null"));

        ViewColumnGroup cg2 = new ViewColumnGroup();
        cg2.setClassName("vw-60");
        cg2.add(new ViewColumn("position").type(ViewColumnType.localizedName));
        cg2.add(new ViewColumn("login").name("login_name"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);
        list.add(cg2);

        result.setRoot(list);
        return result;
    }

    public ViewOption getIndividualOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").sortBoth());
        cg.add(new ViewColumn("bin"));
        cg.add(new ViewColumn("labels").name("individual_labels").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public FilterForm getOrgFilter() {
        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("orgCategory", "org_category").url("/Reference/api/org-categories"));
        filterGroup.addItem(new FilterItem("labels", "organization_labels").url("/Staff/api/organization-labels"));

        filterForm.addGroup(filterGroup);

        return filterForm;
    }

    public FilterForm getEmployeeFilter() {
        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("role").url("/Staff/api/roles"));

        filterForm.addGroup(filterGroup);

        return filterForm;
    }
}
