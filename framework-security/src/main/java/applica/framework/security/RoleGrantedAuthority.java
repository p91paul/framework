package applica.framework.security;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 3/20/13
 * Time: 10:34 AM
 */
public class RoleGrantedAuthority implements org.springframework.security.core.GrantedAuthority {

    private Role role;

    public RoleGrantedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getRole();
    }
}
