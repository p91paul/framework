package applica.framework.security;

import applica.framework.security.authorization.AuthorizationException;
import applica.framework.security.authorization.AuthorizationService;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 25/09/14
 * Time: 11:26
 */
class SecurityInstance {

    private User user;
    private AuthorizationService authorizationService;

    protected SecurityInstance(User user, AuthorizationService authorizationService) {
        this.user = user;
        this.authorizationService = authorizationService;
    }

    public boolean isPermitted(String permission, Object... params) {
        try {
            authorize(permission, params);
        } catch (AuthorizationException e) {
            return false;
        }

        return true;
    }

    public void authorize(String permission, Object... params) throws AuthorizationException {
        authorizationService.authorize(user, permission, params);
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
}
