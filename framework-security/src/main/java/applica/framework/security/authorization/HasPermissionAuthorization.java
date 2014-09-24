package applica.framework.security.authorization;

import applica.framework.security.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 24/09/14
 * Time: 19:24
 */
@Component
public class HasPermissionAuthorization implements Authorization {

    @Override
    public void authorize(User user, String permission, Object... params) {
        Assert.notNull(permission, "permission cannot be null");
        user.getRoles().stream().anyMatch((r) -> r.getPermissions().stream().anyMatch((p) -> permission.equals(p)));
    }

}
