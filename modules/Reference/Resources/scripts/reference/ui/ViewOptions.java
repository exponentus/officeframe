package reference.ui;

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

    public ViewOption getAsOfOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg1 = new ViewColumnGroup();
        cg1.add(new ViewColumn("asOfByDate").name("as_of").sortBoth().type(ViewColumnType.date).format("DD.MM.YYYY"));
        cg1.add(new ViewColumn("name").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg1);

        result.setRoot(list);
        return result;
    }

    public ViewOption getStatisticIndicatorTypeOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("statisticType").name("category").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getDocumentTypeOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getDocumentSubjectOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getControlTypeOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("name").name(""));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getApprovalRouteOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("schema").type(ViewColumnType.translate).sortBoth());
        cg.add(new ViewColumn("on").name("is_on"));
        cg.add(new ViewColumn("category").sortBoth());

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getIndustryTypeOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("category").type(ViewColumnType.localizedName).sortBoth().className("vw-40"));
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getTagOptions() {
        ViewOption result = new ViewOption();
        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth().style("return it.hidden ? {color:it.color,'border-bottom': '1px solid #ccc'} : {color:it.color}"));
        cg.add(new ViewColumn("category"));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public FilterForm getTagFilter() {
        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();

        List<FilterItem.Item> items = new ArrayList<>();
        items.add(new FilterItem.Item("true", "show_hidden", null));
        filterGroup.addItem(new FilterItem("hidden").items(items).typeCheckbox());

        filterForm.addGroup(filterGroup);

        return filterForm;
    }

    // Address
    public ViewOption getDistrictOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("region").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public ViewOption getRegionOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth().style("return it.primary ? { 'font-weight':'bold' } : null"));
        cg.add(new ViewColumn("type").type(ViewColumnType.localizedName));
        cg.add(new ViewColumn("country").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    // Locality
    public ViewOption getLocalityOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("region").type(ViewColumnType.localizedName));
        cg.add(new ViewColumn("district").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public FilterForm getLocalityFilter() {
        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("region", "region").url("/Reference/api/regions"));
        filterGroup.addItem(new FilterItem("district", "district").url("/Reference/api/districts"));

        filterForm.addGroup(filterGroup);

        return filterForm;
    }

    public ViewOption getCityDistrictOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("locality").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    // Street
    public ViewOption getStreetOptions() {
        ViewOption result = new ViewOption();

        ViewColumnGroup cg = new ViewColumnGroup();
        cg.add(new ViewColumn("name").type(ViewColumnType.localizedName).sortBoth());
        cg.add(new ViewColumn("locality").type(ViewColumnType.localizedName));

        List<ViewColumnGroup> list = new ArrayList<>();
        list.add(cg);

        result.setRoot(list);
        return result;
    }

    public FilterForm getStreetFilter() {
        FilterForm filterForm = new FilterForm();
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.addItem(new FilterItem("locality", "locality").url("/Reference/api/localities"));

        filterForm.addGroup(filterGroup);

        return filterForm;
    }
}
