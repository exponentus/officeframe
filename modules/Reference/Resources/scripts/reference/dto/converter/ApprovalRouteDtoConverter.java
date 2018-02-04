package reference.dto.converter;

import com.exponentus.common.dto.converter.GenericConverter;
import reference.model.ApprovalRoute;

public class ApprovalRouteDtoConverter implements GenericConverter<ApprovalRoute, ApprovalRoute> {

    @Override
    public ApprovalRoute apply(ApprovalRoute entity) {
        ApprovalRoute dto = new ApprovalRoute();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setName(entity.getName());
        dto.setSchema(entity.getSchema());
        dto.setOn(entity.isOn());
        dto.setCategory(entity.getCategory());

        return dto;
    }
}
