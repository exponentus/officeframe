package reference.ui;

import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
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
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("region").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
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
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("type").type(ViewColumnType.localizedName));
        cg.add(new ViewColumn("country").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
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
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
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
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
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
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("name").name(""));

        List<ViewColumnGroup> list = new ArrayList<>();
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
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("schema").sortBoth());
        cg.add(new ViewColumn("on").name("is_on"));
        cg.add(new ViewColumn("category").sortBoth());

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }

    public ViewPageOptions getTagOptions() {
        /*
        tags: [{
            columns: [
            { name: 'name', value: 'name', type: 'localizedName', sort: 'both', style: (it: Tag) => { return { color: it.color }; } },
            { name: 'category', value: 'category', type: 'text' }
            ]
        }]*/

        ViewPageOptions result = new ViewPageOptions();
        ViewColumnGroup cg = new ViewColumnGroup();

        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).style("return { color:it.color }").sortBoth());
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }
}
