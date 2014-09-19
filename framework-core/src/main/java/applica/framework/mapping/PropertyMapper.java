package applica.framework.mapping;

import applica.framework.FormDescriptor;
import applica.framework.FormField;
import applica.framework.data.Entity;

import java.util.Map;

public interface PropertyMapper {
    void mapFormValueFromEntityProperty(FormDescriptor formDescriptor, FormField formField, Map<String, Object> values, Entity entity) throws MappingException;

    void mapEntityPropertyFromRequestValue(FormDescriptor formDescriptor, FormField formField, Entity entity, Map<String, String[]> requestValues) throws MappingException;
}
