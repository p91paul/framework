package applica.framework.data;

import applica.framework.ApplicationContextProvider;
import applica.framework.CrudConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 08/10/14
 * Time: 15:24
 */

/**
 * This is the default implementation for repositories factory
 */
public class DefaultRepositoriesFactory implements RepositoriesFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Repository createForEntity(Class<? extends Entity> type) {
        Repository repository = null;

        try {
            repository = applicationContext.getBeansOfType(Repository.class).values().stream()
                    .filter(r -> r.getEntityType().equals(type))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchBeanDefinitionException("repository " + type.getName()));
        } catch (NoSuchBeanDefinitionException e) {
            repository = (DefaultRepository) applicationContext.getBean("default-repository");
            ((DefaultRepository) repository).setEntityType(type);
        }

        return repository;
    }

    @Override
    public Repository create(Class<? extends Repository> type) {
        return applicationContext.getBean(type);
    }

}
