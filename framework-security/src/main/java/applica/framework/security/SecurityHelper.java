package applica.framework.security;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/13
 * Time: 10:27
 */
public class SecurityHelper {

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

    public User getLoggedUser() {
        UserDetailsImpl userDetails = getLoggedUserDetails();

        if(userDetails != null) {
            return userDetails.getUser();
        }

        return null;
    }

    public boolean hasRole(final String role) {
        UserDetailsImpl userDetails = getLoggedUserDetails();
        if(userDetails != null) {
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
            if(CollectionUtils.exists(roles, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ((GrantedAuthority)o).getAuthority().equals(role);
                }
            })) {
                return true;
            }
        }

        return false;
    }

    public boolean isAuthenticated() {
        UserDetailsImpl impl = getLoggedUserDetails();
        return impl != null;
    }

    public boolean hasAnyRole(String... roles) {
        for(String role : roles) {
            if(hasRole(role)) {
                return true;
            }
        }

        return false;
    }
}
