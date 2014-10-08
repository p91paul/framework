package applica.framework.library.forms.processors;

import applica.framework.Form;
import applica.framework.FormProcessException;
import applica.framework.data.Entity;
import applica.framework.library.i18n.Localization;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.Map;

public class BaseFormProcessor extends ValidateableFormProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Map<String, Object> toMap(Form form, Entity entity) throws FormProcessException {
        applicationContext.getBeansOfType(Validator.class).values().stream()
                .filter(r -> r.supports(entity.getClass()))
                .findFirst()
                .ifPresent(v -> setValidator(v));

        Localization localization = new Localization(applicationContext);
        setLocalization(localization);

        return super.toMap(form, entity);
    }


}
