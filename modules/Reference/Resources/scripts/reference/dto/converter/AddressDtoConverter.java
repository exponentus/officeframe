package reference.dto.converter;

import com.exponentus.common.dto.converter.GenericConverter;
import com.exponentus.common.ui.TreeNode;
import reference.model.Country;
import reference.model.District;
import reference.model.Region;

import java.util.LinkedList;
import java.util.List;

public class AddressDtoConverter implements GenericConverter<Country, TreeNode> {

    @Override
    public TreeNode apply(Country country) {
        TreeNode root = new TreeNode();

        root.setTitle(country.getTitle());
        // root.setData(country);

        List<TreeNode> children = new LinkedList<>();
        root.setChildren(children);

        for (Region region : country.getRegions()) {
            TreeNode regionChild = root.addChildren(region);
            regionChild.setData(null);
            regionChild.setTitle(region.getTitle());
            for (District district : region.getDistricts()) {
                TreeNode districtChild = regionChild.addChildren(district);
                districtChild.setTitle(district.getTitle());
                districtChild.setData(null);
            }
        }

        return root;
    }
}
