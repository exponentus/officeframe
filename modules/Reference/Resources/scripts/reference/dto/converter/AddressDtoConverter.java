package reference.dto.converter;

import com.exponentus.common.dto.converter.GenericConverter;
import com.exponentus.common.ui.TreeNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import reference.model.Country;
import reference.model.District;
import reference.model.Region;
import reference.model.embedded.Address;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AddressDtoConverter extends StdSerializer<Address> implements GenericConverter<Country, TreeNode> {

    public AddressDtoConverter() {
        this(null);
    }

    public AddressDtoConverter(Class<Address> t) {
        super(t);
    }

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

    @Override
    public void serialize(Address address, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        Country country = address.getCountry();
        if (country != null) {
            Country c = new Country();
            c.setId(country.getId());
            c.setName(country.getName());
            c.setLocName(country.getLocName());
            gen.writeObjectField("country", c);
        }
        Region region = address.getRegion();
        if (region != null) {
            Region r = new Region();
            r.setId(region.getId());
            r.setName(region.getName());
            r.setLocName(region.getLocName());
            gen.writeObjectField("region", r);
        }
        District district = address.getDistrict();
        if (district != null) {
            District r = new District();
            r.setId(district.getId());
            r.setName(district.getName());
            r.setLocName(district.getLocName());
            gen.writeObjectField("district", r);
        }
        gen.writeObjectField("coordinates", address.getCoordinates());
        gen.writeEndObject();
    }
}
