package applica.framework.data;

/**
 * Allowed filters
 * like: username
 */
public interface Repository<T extends Entity> {

    T get(Object id);
    LoadResponse<T> find(LoadRequest request);
    void save(T entity);
    void delete(Object id);
    Class<T> getEntityType();

}
