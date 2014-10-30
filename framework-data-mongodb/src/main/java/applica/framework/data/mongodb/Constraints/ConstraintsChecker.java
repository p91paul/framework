package applica.framework.data.mongodb.Constraints;

import applica.framework.data.Entity;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 30/10/14
 * Time: 11:41
 */
public interface ConstraintsChecker {

    void check(Entity entity) throws ConstraintException;

}
