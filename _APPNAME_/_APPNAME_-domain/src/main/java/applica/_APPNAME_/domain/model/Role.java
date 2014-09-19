package applica._APPNAME_.domain.model;

import applica.framework.data.SEntity;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
