package applica.framework.processors;

import applica.framework.Form;
import applica.framework.FormProcessException;
import applica.framework.ValidationResult;
import applica.framework.data.Entity;

import java.util.Map;

public interface FormProcessor {
    Entity toEntity(Form form, Class<? extends Entity> type, Map<String, String[]> requestValues, ValidationResult validationResult) throws FormProcessException;

    Map<String, Object> toMap(Form form, Entity entity) throws FormProcessException;
}
