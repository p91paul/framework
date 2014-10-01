package applica.framework.data.helpers;

import applica.framework.library.options.OptionsManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 01/10/14
 * Time: 18:55
 */
public class HibernateHelper {

    @Autowired
    private OptionsManager options;

    private SessionFactory sessionFactory;

    public SessionFactory createSessionFactory() {
        return null;
    }

}
