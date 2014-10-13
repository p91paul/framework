package applica._APPNAME_.data.mongodb;

import applica.framework.data.Entity;
import applica.framework.data.Sort;
import applica.framework.data.mongodb.MongoRepository;
import applica._APPNAME_.domain.data.UsersRepository;
import applica._APPNAME_.domain.model.User;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 28/10/13
 * Time: 17:22
 */
@Repository
public class UsersMongoRepository extends MongoRepository<User> implements UsersRepository {

    @Override
    public Class<User> getEntityType() {
        return User.class;
    }

    @Override
    public List<Sort> getDefaultSorts() {
        return Arrays.asList(new Sort("mail", false));
    }
}
