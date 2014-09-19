package applica.framework.library.utils;

import java.util.Objects;
import java.util.function.Function;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 01/04/14
 * Time: 19:17
 */
public class Quiet {

    public static Exception exec(Action action) {
        Exception ex = null;
        Objects.requireNonNull(action, "Action cannot be null");
        try {
            action.act();
        } catch(Exception innerE) {
            ex = innerE;
        }

        return ex;
    }

}
