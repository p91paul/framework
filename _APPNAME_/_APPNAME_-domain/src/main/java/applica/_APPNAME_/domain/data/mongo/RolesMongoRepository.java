package applica._APPNAME_.domain.data.mongo;

import applica.framework.data.Entity;
import applica.framework.data.Sort;
import applica.framework.data.mongodb.MongoRepository;
import applica._APPNAME_.domain.data.RolesRepository;
import applica._APPNAME_.domain.model.Role;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 3/3/13
 * Time: 10:53 PM
 */
@Repository
public class RolesMongoRepository extends MongoRepository implements RolesRepository {

    @Override
    protected String getCollectionName() {
        return "roles";
    }

    @Override
    protected Class<? extends Entity> getType() {
        return Role.class;
    }

    @Override
    public Sort getDefaultSort() {
        return new Sort("role", false);
    }
}
