package security.models.users;

import security.config.security.ApplicationUserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public
class RolesConverter implements AttributeConverter<List<ApplicationUserRole>, String> {

    @Override
    public String convertToDatabaseColumn(List<ApplicationUserRole> roles) {
        if (roles.size() == 0) return "";
        return roles.stream()
                .map(rol -> String.valueOf(rol.ordinal()))
                .collect(Collectors.joining(","));
    }

    @Override
    public List<ApplicationUserRole> convertToEntityAttribute(String rolesEncoded) {
        if (rolesEncoded.equals("")) return new ArrayList<>();
        return Arrays.stream(rolesEncoded.split(","))
                .map(Integer::valueOf)
                .map(role -> ApplicationUserRole.values()[role]).collect(Collectors.toList());
    }
}
