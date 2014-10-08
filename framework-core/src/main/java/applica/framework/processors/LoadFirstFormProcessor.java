package applica.framework.processors;

import applica.framework.*;
import applica.framework.data.Entity;
import applica.framework.data.Repository;
import applica.framework.mapping.MappingException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 07/11/13
 * Time: 09:49
 */
public class LoadFirstFormProcessor extends SimpleFormProcessor {

    @Override
    protected Entity instantiateEntity(Form form, Class<? extends Entity> type, Map<String, String[]> requestValues, ValidationResult validationResult) throws FormProcessException {
        Entity entity = super.instantiateEntity(form, type, requestValues, validationResult);
        if (requestValues.containsKey("id")) {
            try {
                BeanUtils.setProperty(entity, "id", requestValues.get("id"));
            } catch (Exception e) {}
        }
        if(entity.getId() != null) {
            Entity persistentEntity = null;
            try {
                Repository repository = CrudConfiguration.instance().getFormRepository(type);
                if(repository != null) {
                    persistentEntity = repository.get(entity.getId());
                    if(persistentEntity != null) {
                        return persistentEntity;
                    }
                }
            } catch (CrudConfigurationException e) {
                //in case of error, return standard entity
            }
        }

        return entity;
    }
}
