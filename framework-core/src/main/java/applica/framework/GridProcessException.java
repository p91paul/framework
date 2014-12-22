package applica.framework;

/**
 *
 * @author Paolo Inaudi
 */
public class GridProcessException extends Exception {

    private static final long serialVersionUID = 8120843897631290297L;

    public GridProcessException() {
        super();
    }

    public GridProcessException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GridProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public GridProcessException(String message) {
        super(message);
    }

    public GridProcessException(Throwable cause) {
        super(cause);
    }
}
