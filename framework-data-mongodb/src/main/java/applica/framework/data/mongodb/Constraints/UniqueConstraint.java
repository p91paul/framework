package applica.framework.data.mongodb.Constraints;

import applica.framework.data.Entity;
import applica.framework.data.LoadRequest;
import applica.framework.data.RepositoriesFactory;
import applica.framework.data.Repository;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 30/10/14
 * Time: 11:33
 */
public abstract class UniqueConstraint<T extends Entity> implements Constraint<T> {

    @Autowired
    private RepositoriesFactory repositoriesFactory;

    public RepositoriesFactory getRepositoriesFactory() {
        return repositoriesFactory;
    }

    public void setRepositoriesFactory(RepositoriesFactory repositoriesFactory) {
        this.repositoriesFactory = repositoriesFactory;
    }

    @Override
    public void check(T entity) throws ConstraintException {
        Objects.requireNonNull(entity, "Entity cannot be null");
        Objects.requireNonNull(getProperty(), "Property cannot be null");
        Repository<T> repository = repositoriesFactory.createForEntity(entity.getClass());
        Object value = null;
        try {
            value = PropertyUtils.getProperty(entity, getProperty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (value != null) {
            for (Entity checkEntity : repository.find(LoadRequest.build()).getRows()) {
                if (!checkEntity.getId().equals(entity.getId())) {
                    Object checkValue = null;
                    try {
                        checkValue = PropertyUtils.getProperty(checkEntity, getProperty());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    if(value.equals(checkValue)) {
                        throw new ConstraintException(String.format("%s.%s is not unique", getType().getName(), getProperty()));
                    }
                }
            }
        }
    }

}
