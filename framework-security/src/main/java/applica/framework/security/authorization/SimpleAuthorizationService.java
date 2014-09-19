package applica.framework.security.authorization;

import applica.framework.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/13
 * Time: 10:59
 */
public class SimpleAuthorizationService implements AuthorizationService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void authorize(User user, Class<? extends AuthorizationContext> contextType, String authorization, Object... parameters) throws AuthorizationException {
        AuthorizationContext authorizationContext = (AuthorizationContext) applicationContext.getBean(contextType);
        authorizationContext.authorize(user, authorization, parameters);
    }

}
