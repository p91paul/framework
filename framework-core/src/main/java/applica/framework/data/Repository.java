package applica.framework.data;

/**
 * Allowed filters
 * like: username
 */
public interface Repository {

    Entity get(Object id);
    LoadResponse find(LoadRequest request);
    void save(Entity entity);
    void delete(Object id);

}
