package applica.framework.security;

import applica.framework.ApplicationContextProvider;
import applica.framework.security.authorization.AuthorizationException;
import applica.framework.security.authorization.AuthorizationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 31/10/13
 * Time: 10:27
 */

/**
 * Manage security for users. Use with() static method to specify user, otherwhise security is for currently logged user
 * Don't use autowire with this class, because is needed also in non DI contexts (example: velocity)
 */
public class Security {

    private AuthorizationService authorizationService;
    private SecurityInstance securityInstance;

    private static Security s_instance = null;
    private static Map<Object, Security> instances = new HashMap<>();

    /**
     * Gets security instance for logged user.
     * @return
     */
    public static Security withMe() {
        if (s_instance == null) {
            s_instance = new Security();
        }

        return s_instance;
    }

    /**
     * Gets security instance for specified user. Methods like isAuthenticated() have not sense.
     * @param user
     * @return
     */
    public static Security with(User user) {
        instances.putIfAbsent(user.getUsername(), new Security(new SecurityInstance(user, null)));
        return instances.get(user.getUsername());
    }

    private Security(SecurityInstance instance) {
        this.securityInstance = instance;
        this.securityInstance.setAuthorizationService(getAuthorizationService());
    }

    public Security() {}

    public UserDetailsImpl getLoggedUserDetails() {
        UserDetailsImpl userDetails = null;

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null) {

            if(authentication.getPrincipal() instanceof UserDetailsImpl) {
                userDetails = (UserDetailsImpl) authentication.getPrincipal();
            } else if(authentication.getDetails() instanceof  UserDetailsImpl) {
                userDetails = (UserDetailsImpl) authentication.getDetails();
            }
        }

        return userDetails;
    }

    protected AuthorizationService getAuthorizationService() {
        if (authorizationService == null) {
            authorizationService = ApplicationContextProvider.provide().getBean(AuthorizationService.class);
        }

        return authorizationService;
    }

    public User getLoggedUser() {
        UserDetailsImpl userDetails = getLoggedUserDetails();

        if(userDetails != null) {
            return userDetails.getUser();
        }

        return null;
    }

    public boolean isAuthenticated() {
        UserDetailsImpl impl = getLoggedUserDetails();
        return impl != null;
    }

    private SecurityInstance getInstance() {
        if (securityInstance == null) {
            securityInstance = new SecurityInstance(getLoggedUser(), getAuthorizationService());
        }

        return securityInstance;
    }

    public boolean isPermitted(String permission, Object... params) {
        return getInstance().isPermitted(permission, params);
    }

    public void authorize(String permission, Object... params) throws AuthorizationException {
        getInstance().authorize(permission, params);
    }

}
