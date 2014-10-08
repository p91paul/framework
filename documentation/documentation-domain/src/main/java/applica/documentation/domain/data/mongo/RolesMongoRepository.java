package applica.documentation.domain.data.mongo;

import applica.documentation.domain.data.RolesRepository;
import applica.documentation.domain.model.Role;
import applica.framework.data.Entity;
import applica.framework.data.Sort;
import applica.framework.data.mongodb.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicadoit.com)
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
