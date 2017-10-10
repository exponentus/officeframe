package reference.ui;

import com.exponentus.common.ui.view.ColumnOption;
import com.exponentus.common.ui.view.ColumnOptionGroup;
import com.exponentus.common.ui.view.ViewPageOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewOptions {

    public ViewPageOptions getDistrictOptions() {
        /*
        districts: [{
            columns: [
                { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                { name: 'region', value: 'region', type: 'localizedName' }
            ]
        }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.getColumns().add(new ColumnOption("name", "name", "localizedName").sort("both"));
        cg.getColumns().add(new ColumnOption("region", "region", "localizedName"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getRegionOptions() {
        /*
        regions: [{
            columns: [
                { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                { name: 'type', value: 'type', type: 'localizedName' },
                { name: 'country', value: 'country', type: 'localizedName' }
            ]
        }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.getColumns().add(new ColumnOption("name", "name", "localizedName").sort("both"));
        cg.getColumns().add(new ColumnOption("type", "type", "localizedName"));
        cg.getColumns().add(new ColumnOption("country", "country", "localizedName"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getDocumentTypeOptions() {
        /*
        'documentType': [{
            columns: [
                { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                { name: 'category', value: 'category', type: 'text' }
            ]
        }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.getColumns().add(new ColumnOption("name", "name", "localizedName").sort("both"));
        cg.getColumns().add(new ColumnOption("category", "category", "text"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getDocumentSubjectOptions() {
        /*
        'document-subjects': [{
            columns: [
                { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                { name: 'category', value: 'category', type: 'text' }
            ]
        }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.getColumns().add(new ColumnOption("name", "name", "localizedName").sort("both"));
        cg.getColumns().add(new ColumnOption("category", "category", "text"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getControlTypeOptions() {
        /*
        'control-types': [{
            columns: [
                { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                { value: 'name', type: 'text' }
            ]
        }],
         */

        ViewPageOptions result = new ViewPageOptions();
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.getColumns().add(new ColumnOption("name", "name", "localizedName").sort("both"));
        cg.getColumns().add(new ColumnOption("", "name", "text"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getApprovalRouteOptions() {
        /*
        'approval-routes': [{
            columns: [
                { name: 'name', value: 'name', type: 'localizedName', sort: 'both' },
                { name: 'schema', value: 'schema', sort: 'both' },
                { name: 'is_on', value: 'on' },
                { name: 'category', value: 'category', sort: 'both' }
            ]
        }]
         */

        ViewPageOptions result = new ViewPageOptions();
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.getColumns().add(new ColumnOption("name", "name", "localizedName").sort("both"));
        cg.getColumns().add(new ColumnOption("schema", "schema", "text").sort("both"));
        cg.getColumns().add(new ColumnOption("is_on", "on", "text"));
        cg.getColumns().add(new ColumnOption("category", "category", "text").sort("both"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }
}
