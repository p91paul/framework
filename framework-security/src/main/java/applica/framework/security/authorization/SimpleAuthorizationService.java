package applica.framework.security.authorization;

import applica.framework.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
    public void authorize(User user, String permission, Object... parameters) throws AuthorizationException {
        Assert.notNull(user, "SimpleAuthorizationService: user must be specified");
        Assert.isTrue(StringUtils.hasLength(permission), "SimpleAuthorizationService: permission must be specified");

        String[] elements = permission.split(":");
        Assert.isTrue(elements.length >= 2, "Bad permission format: " + permission);

        String context = elements[0];
        String authorization = elements[1];

        AuthorizationContext authorizationContext = (AuthorizationContext) applicationContext.getBean("authorization-context-" + context);
        authorizationContext.authorize(user, authorization, parameters);
    }

}
