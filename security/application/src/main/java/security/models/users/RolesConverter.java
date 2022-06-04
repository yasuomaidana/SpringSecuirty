package security.models.users;

import org.springframework.util.StringUtils;
import security.config.security.ApplicationUserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;
import java.util.stream.Collectors;

@Converter
public
class RolesConverter implements AttributeConverter<Set<ApplicationUserRole>, String> {

    @Override
    public String convertToDatabaseColumn(Set<ApplicationUserRole> roles) {
        if (roles.size() == 0) return "";
        return roles.stream()
                .map(rol -> String.valueOf(rol.ordinal()))
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<ApplicationUserRole> convertToEntityAttribute(String rolesEncoded) {
        if (!StringUtils.hasLength(rolesEncoded)) return null;
        return Arrays.stream(rolesEncoded.split(","))
                .map(Integer::valueOf)
                .map(role -> ApplicationUserRole.values()[role]).collect(Collectors.toSet());
    }
}
