package applica.documentation.admin.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 20/02/14
 * Time: 13:25
 */
@Component("validator-article")
public class ArticleValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
