package applica._APPNAME_.data.hibernate;

import applica.framework.data.Sort;
import applica.framework.data.hibernate.HibernateRepository;
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
public class UsersHibernateRepository extends HibernateRepository<User> implements UsersRepository {

    @Override
    public Class<User> getEntityType() {
        return User.class;
    }

    @Override
    public Sort getDefaultSort() {
        return new Sort("mail", false);
    }

}