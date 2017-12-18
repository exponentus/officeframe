package monitoring.dto.converter;

import administrator.model.User;
import com.exponentus.common.converter.GenericConverter;
import monitoring.model.UserActivity;

public class UserActivityDtoConverter implements GenericConverter<UserActivity, UserActivity> {

    @Override
    public UserActivity apply(UserActivity entity) {
        UserActivity dto = new UserActivity();

        dto.setId(entity.getId());
        dto.setEventTime(entity.getEventTime());
        dto.setIp(entity.getIp());
        dto.setCountry(entity.getCountry());
        dto.setType(entity.getType());

        User actUser = new User();
        actUser.setLogin(entity.getActUser().getLogin());
        dto.setActUser(actUser);

        return dto;
    }
}
