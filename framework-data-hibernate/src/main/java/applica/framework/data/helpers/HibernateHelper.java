package applica.framework.data.helpers;

import applica.framework.library.options.OptionsManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;


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

    public void createSessionFactory() {
        if (sessionFactory == null) {
            Properties prop = new Properties();
            prop.setProperty("hibernate.connection.url", options.get("applica.framework.data.hibernate.connection.url"));
            prop.setProperty("hibernate.connection.username", options.get("applica.framework.data.hibernate.connection.username"));
            prop.setProperty("hibernate.connection.password", options.get("applica.framework.data.hibernate.connection.password"));
            prop.setProperty("dialect", options.get("applica.framework.data.hibernate.dialect"));

            sessionFactory = new Configuration()
                    .addProperties(prop)
                    .addPackage(options.get("applica.framework.data.hibernate.package"))
                    .buildSessionFactory(new StandardServiceRegistryBuilder().build());
        }
    }

}
