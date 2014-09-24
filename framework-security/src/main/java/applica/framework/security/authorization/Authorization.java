package applica.framework.security.authorization;

import applica.framework.security.User;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 24/09/14
 * Time: 19:22
 */
@FunctionalInterface
public interface Authorization {

    void authorize(User user, String permission, Object... params);

}
