package applica.documentation.domain.data.mongo;

import applica.documentation.domain.data.UsersRepository;
import applica.documentation.domain.model.User;
import applica.framework.data.Entity;
import applica.framework.data.Sort;
import applica.framework.data.mongodb.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 28/10/13
 * Time: 17:22
 */
@Repository
public class UsersMongoRepository extends MongoRepository implements UsersRepository {

    @Override
    protected String getCollectionName() {
        return "users";
    }

    @Override
    protected Class<? extends Entity> getType() {
        return User.class;
    }

    @Override
    public Sort getDefaultSort() {
        return new Sort("description", false);
    }

}
