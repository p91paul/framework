package applica.framework.data;

/**
 * The base object for all framework components
 *
 * @param <T> The type of the Entity key
 */
public interface Entity<T> {

    T getId();

    void setId(T id);
}
