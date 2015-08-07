package applica.framework.processors;

import applica.framework.Form;
import applica.framework.FormProcessException;
import applica.framework.ValidationResult;
import applica.framework.data.Entity;
import applica.framework.mapping.FormDataMapper;
import applica.framework.mapping.MappingException;
import applica.framework.mapping.SimpleFormDataMapper;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleFormProcessor implements FormProcessor {

    protected Log logger = LogFactory.getLog(getClass());

    protected Entity instantiateEntity(Form form, Class<? extends Entity> type, Map<String, String[]> requestValues,
            ValidationResult validationResult) throws FormProcessException {
        Entity entity = null;

        try {
            entity = type.newInstance();
        } catch (Exception e) {
            logger.error("Error instanziating entity:" + type.getName());
            throw new FormProcessException("Error instanziating entity", e);
        }

        return entity;
    }

    @Override
    public Entity toEntity(Form form, Class<? extends Entity> type, Map<String, String[]> requestValues,
            ValidationResult validationResult) throws FormProcessException {
        Entity entity = instantiateEntity(form, type, requestValues, validationResult);

        FormDataMapper mapper = getFormDataMapper();
        try {
            mapper.mapEntityFromRequestValues(form.getDescriptor(), entity, requestValues);
        } catch (MappingException e) {
            validationResult.rejectValue(e.getProperty(), e.getCause());
        }

        if (validationResult != null) {
            validate(entity, validationResult);
        }

        return entity;
    }

    protected void validate(Entity entity, ValidationResult validationResult) {
    }

    @Override
    public Map<String, Object> toMap(Form form, Entity entity) throws FormProcessException {
        Map<String, Object> values = new HashMap<>();
        FormDataMapper mapper = getFormDataMapper();
        try {
            mapper.mapFormValuesFromEntity(form.getDescriptor(), values, entity);
        } catch (MappingException e) {
            throw new FormProcessException(e);
        }

        return values;
    }

    protected FormDataMapper getFormDataMapper() {
        SimpleFormDataMapper mapper = new SimpleFormDataMapper();
        return mapper;
    }
}
