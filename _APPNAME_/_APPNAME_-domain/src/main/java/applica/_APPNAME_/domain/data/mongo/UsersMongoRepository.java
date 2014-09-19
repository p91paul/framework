package applica._APPNAME_.domain.data.mongo;

import applica.framework.data.Entity;
import applica.framework.data.Sort;
import applica.framework.data.mongodb.MongoRepository;
import applica._APPNAME_.domain.data.UsersRepository;
import applica._APPNAME_.domain.model.User;
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
