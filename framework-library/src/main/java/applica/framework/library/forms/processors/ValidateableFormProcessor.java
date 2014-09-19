package applica.framework.library.forms.processors;

import applica.framework.ValidationResult;
import applica.framework.data.Entity;
import applica.framework.library.i18n.Localization;
import applica.framework.processors.LoadFirstFormProcessor;
import applica.framework.processors.SimpleFormProcessor;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

public class ValidateableFormProcessor extends LoadFirstFormProcessor {
    private Validator validator;
    private Localization localization;

    public Localization getLocalization() {
        return localization;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    protected void validate(Entity entity, ValidationResult validationResult) {
        if (validator != null) {
            Map<Object, Object> map = new HashMap<>();
            MapBindingResult result = new MapBindingResult(map, entity.getClass().getName());
            validator.validate(entity, result);

            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    FieldError ferror = (FieldError) error;
                    if (ferror != null) {
                        String message = error.getDefaultMessage();
                        if (localization != null) {
                            message = localization.getMessage(message);
                        }
                        validationResult.rejectValue(ferror.getField(), message);
                    }
                }
            }
        }
    }
}
