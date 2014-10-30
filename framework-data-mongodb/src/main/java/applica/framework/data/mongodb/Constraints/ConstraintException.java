package applica.framework.data.mongodb.Constraints;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 30/10/14
 * Time: 11:30
 */
public class ConstraintException extends Exception {

    public ConstraintException() {
    }

    public ConstraintException(String message) {
        super(message);
    }

    public ConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintException(Throwable cause) {
        super(cause);
    }

    public ConstraintException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
