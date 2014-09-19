package applica.framework.mapping;

import applica.framework.FormDescriptor;
import applica.framework.data.Entity;

import java.util.Map;

public interface FormDataMapper {
    void mapFormValuesFromEntity(FormDescriptor formDescriptor, Map<String, Object> values, Entity entity) throws MappingException;

    void mapEntityFromRequestValues(FormDescriptor formDescriptor, Entity entity, Map<String, String[]> requestValues) throws MappingException;
}
