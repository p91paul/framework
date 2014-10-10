package applica._APPNAME_.data.hibernate;

import applica.framework.data.Sort;
import applica.framework.data.hibernate.HibernateRepository;
import applica._APPNAME_.domain.data.RolesRepository;
import applica._APPNAME_.domain.model.Role;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 3/3/13
 * Time: 10:53 PM
 */
@Repository
public class RolesHibernateRepository extends HibernateRepository<Role> implements RolesRepository {

    @Override
    public Sort getDefaultSort() {
        return new Sort("role", false);
    }

    @Override
    public Class<Role> getEntityType() {
        return Role.class;
    }

}
