package security.models.users;

import security.config.security.ApplicationUserPermission;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public
class PermissionsConverter implements AttributeConverter<List<ApplicationUserPermission>, String> {

    @Override
    public String convertToDatabaseColumn(List<ApplicationUserPermission> permissions) {
        if (permissions.size() == 0) return "";
        return permissions.stream()
                .map(rol -> String.valueOf(rol.ordinal()))
                .collect(Collectors.joining(","));
    }

    @Override
    public List<ApplicationUserPermission> convertToEntityAttribute(String permissionsEncoded) {
        if (permissionsEncoded.equals("")) return new ArrayList<>();
        return Arrays.stream(permissionsEncoded.split(","))
                .map(Integer::valueOf)
                .map(role -> ApplicationUserPermission.values()[role]).collect(Collectors.toList());
    }
}
