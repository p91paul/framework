package applica.framework.security.tests.auth;

import applica.framework.security.annotations.AuthorizationContext;
import applica.framework.security.annotations.Permission;
import applica.framework.security.authorization.AuthorizationException;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 25/09/14
 * Time: 13:03
 */
@AuthorizationContext("crud")
public class CrudAuthorizationContext {

    @Permission("create")
    public void create() throws AuthorizationException {

    }

    @Permission("update")
    public void update() throws AuthorizationException {

    }

    @Permission("delete")
    public void delete() throws AuthorizationException {

    }
}
