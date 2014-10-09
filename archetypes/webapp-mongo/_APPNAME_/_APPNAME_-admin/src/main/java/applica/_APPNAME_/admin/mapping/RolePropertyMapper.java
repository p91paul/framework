package applica._APPNAME_.admin.mapping;

import applica._APPNAME_.domain.model.Role;
import applica._APPNAME_.domain.model.User;
import applica.framework.FormDescriptor;
import applica.framework.FormField;
import applica.framework.data.Entity;
import applica.framework.library.utils.NullSafe;
import applica.framework.mapping.MappingException;
import applica.framework.mapping.PropertyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 06/11/13
 * Time: 15:55
 */
@Component
public class RolePropertyMapper implements PropertyMapper {
    
    @Override
    public void mapFormValueFromEntityProperty(FormDescriptor formDescriptor, FormField formField, Map<String, Object> values, Entity entity) throws MappingException {
        final User user = (User) entity;
        values.put("roles", NullSafe.get(new NullSafe.NullSafeFunc<Object>() {
            @Override
            public Object eval() {
                return user.getRoles();
            }
        }));
    }

    @Override
    public void mapEntityPropertyFromRequestValue(FormDescriptor formDescriptor, FormField formField, Entity entity, Map<String, String[]> requestValues) throws MappingException {
        User user = (User)entity;
        List<Role> roles = new ArrayList<>();
        String[] rolesArray = requestValues.get("roles");
        if (rolesArray != null) {
            for (String roleName : rolesArray) {
                Role role = new Role();
                role.setRole(roleName);
                roles.add(role);
            }
            user.setRoles(roles);
        }
    }
    
}
