package applica.framework.data.mongodb.tests.data;

import applica.framework.data.LoadRequest;
import applica.framework.data.LoadResponse;
import applica.framework.data.Repository;
import applica.framework.data.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:44
 */
public abstract class MockRepository<T extends Entity> implements Repository<T> {

    private List<T> entities = new ArrayList<>();

    @Override
    public Optional<T> get(Object id) {
        return entities.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    @Override
    public LoadResponse<T> find(LoadRequest request) {
        LoadResponse<T> response = new LoadResponse<>();
        response.setTotalRows(entities.size());
        response.setRows(entities);
        return response;
    }

    @Override
    public void save(T entity) {
        entities.add(entity);
    }

    @Override
    public void delete(Object id) {

    }
}
