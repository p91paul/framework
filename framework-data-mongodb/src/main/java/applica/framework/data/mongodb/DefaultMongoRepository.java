package applica.framework.data.mongodb;

import applica.framework.data.DefaultRepository;
import applica.framework.data.Entity;
import applica.framework.data.Repository;
import applica.framework.utils.Strings;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 08/10/14
 * Time: 14:52
 */
public class DefaultMongoRepository<T extends Entity> extends MongoRepository<T> implements Repository<T>, DefaultRepository<T> {

    private Class<T> entityType;

    @Override
    public Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public void setEntityType(Class<T> entityType) {
        this.entityType = entityType;
    }
}
