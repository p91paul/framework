package applica.framework.security.authorization;

import applica.framework.security.User;

/**
 * Created with IntelliJ IDEA.
 * User: iaco
 * Date: 04/10/13
 * Time: 18.00
 * To change this template use File | Settings | File Templates.
 */
public interface AuthorizationContext {

    public void authorize(User user, String authorization, Object... parameters) throws AuthorizationException;

}
