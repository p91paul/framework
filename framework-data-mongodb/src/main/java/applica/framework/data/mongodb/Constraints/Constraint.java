package applica.framework.data.mongodb.Constraints;

import applica.framework.data.Entity;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 30/10/14
 * Time: 11:29
 */
public interface Constraint<T extends Entity> {

    Class<T> getType();
    String getProperty();
    void check(T entity) throws ConstraintException;

}
