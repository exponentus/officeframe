package staff.ui;

import com.exponentus.common.ui.filter.FilterForm;
import com.exponentus.common.ui.filter.FilterGroup;
import com.exponentus.common.ui.filter.FilterItem;
import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getOrgOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("orgCategory").name("org_category").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getDepOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("organization").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getEmpOptions() {
        ViewPageOptions result = new ViewPageOptions();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").sortBoth().style("return it.fired ? { 'orgColor': '#777', 'text-decoration': 'line-through' } : null"));
        cg.add(new ViewColumn("position").type(ViewColumnType.localizedName));
        cg.add(new ViewColumn("login").name("login_name"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewPageOptions getIndividualOptions() {
        ViewPageOptions result = new ViewPageOptions();

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
        /*
        organizations: {
            fieldsets: [{
                fields: [{
                    type: 'select',
                    name: 'orgCategory',
                    placeHolder: 'org_category',
                    values: {
                        url: REFERENCE_URL.API_ORG_CATEGORIES,
                        targetValue: 'id',
                        searchable: false
                    }
                }, {
                    type: 'select',
                    name: 'labels',
                    placeHolder: 'organization_labels',
                    values: {
                        url: STAFF_URL.API_ORGANIZATION_LABELS,
                        targetValue: 'id',
                        searchable: false
                    }
                }]
            }]
        }
         */
        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("orgCategory", "org_category").url("/Reference/api/org-categories"));
        filterGroup.addItem(new FilterItem("labels", "organization_labels").url("/Staff/api/organization-labels"));

        filterForm.addGroup(filterGroup);

        return filterForm;
    }

    public FilterForm getEmployeeFilter() {
        /*
            employees: {
                fieldsets: [{
                    fields: [{
                        type: 'select',
                        name: 'role',
                        placeHolder: 'roles',
                        values: {
                            multiple: false,
                            url: STAFF_URL.API_ROLES,
                            targetValue: 'id'
                        }
                    }]
                }]
            }
         */
        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("role").url("/Staff/api/roles"));

        filterForm.addGroup(filterGroup);

        return filterForm;
    }
}
