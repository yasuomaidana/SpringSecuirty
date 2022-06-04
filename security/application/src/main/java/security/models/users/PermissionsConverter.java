package security.models.users;

import org.springframework.util.StringUtils;
import security.config.security.ApplicationUserPermission;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public
class PermissionsConverter implements AttributeConverter<Set<ApplicationUserPermission>, String> {

    @Override
    public String convertToDatabaseColumn(Set<ApplicationUserPermission> permissions) {
        if (permissions.size() == 0) return "";
        return permissions.stream()
                .map(rol -> String.valueOf(rol.ordinal()))
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<ApplicationUserPermission> convertToEntityAttribute(String permissionsEncoded) {
        if (!StringUtils.hasLength(permissionsEncoded)) return null;
        return Arrays.stream(permissionsEncoded.split(","))
                .map(Integer::valueOf)
                .map(role ->
                        ApplicationUserPermission.values()[role])
                .collect(Collectors.toSet());
    }
}
