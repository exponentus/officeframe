package staff.ui;

import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getOrgOptions() {
        /*
            organizations: [{
                columns: [
                    { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                    { name: 'org_category', value: 'orgCategory', type: 'localizedName' }
                ]
            }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("orgCategory").name("org_category").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getDepOptions() {
        /*
            departments: [{
                columns: [
                    { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                    { name: 'organization', value: 'organization', type: 'localizedName' }
                ]
            }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("organization").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getEmpOptions() {
        /*
            employees: [{
                columns: [
                    {
                        name: 'name', value: 'name', type: 'text', sort: 'both',
                        style: (emp: Employee) => {
                            if (emp.allRoles && emp.allRoles.length && emp.allRoles.indexOf('fired') != -1) {
                                return {
                                    'color': '#777',
                                    'text-decoration': 'line-through'
                                };
                            }
                            return null;
                        }
                    },
                    { name: 'position', value: 'position', type: 'localizedName' },
                    { name: 'login_name', value: 'login', type: 'text' }
                ]
            }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").sortBoth().style("return it.fired ? { 'color': '#777', 'text-decoration': 'line-through' } : null"));
        cg.add(new ViewColumn("position").type(ViewColumnType.localizedName));
        cg.add(new ViewColumn("login").name("login_name"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getIndividualOptions() {
        /*
            individuals: [{
                columns: [
                    { name: 'name', value: 'name', sort: 'both' },
                    { name: 'bin', value: 'bin' },
                    { name: 'individual_labels', value: 'labels', type: 'localizedName' }
                ]
            }]
         */

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").sortBoth());
        cg.add(new ViewColumn("bin"));
        cg.add(new ViewColumn("labels").name("individual_labels").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }
}
