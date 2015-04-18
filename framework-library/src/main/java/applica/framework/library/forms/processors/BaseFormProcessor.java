package applica.framework.library.forms.processors;

import applica.framework.CrudConfiguration;
import applica.framework.Form;
import applica.framework.FormProcessException;
import applica.framework.ValidationResult;
import applica.framework.data.Entity;
import applica.framework.library.i18n.Localization;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Validator;

public class BaseFormProcessor extends ValidateableFormProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Entity toEntity(Form form,
            Class<? extends Entity> type, Map<String, String[]> requestValues, ValidationResult validationResult) throws
            FormProcessException {
        Class<? extends Entity> entityType = CrudConfiguration.instance()
                .getFormTypeFromIdentifier(form.getIdentifier());

        Validator validator = applicationContext.getBeansOfType(Validator.class).values().stream()
                .filter(r -> r.supports(entityType))
                .findFirst().orElse(null);
        setValidator(validator);

        Localization localization = new Localization(applicationContext);
        setLocalization(localization);
        return super.toEntity(form, type, requestValues, validationResult);
    }

}
