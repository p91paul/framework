package applica.documentation.domain.model;

import applica.framework.data.SEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Applica
 * User: bimbobruno
 * Date: 3/3/13
 * Time: 10:46 PM
 */
public class Role extends SEntity implements applica.framework.security.Role {

    public static final String ADMIN = "admin";
    public static final String USER = "user";

    private String role;
    private List<String> permissions = new ArrayList<>();

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

}
