package applica.documentation.domain.validation;

import applica.documentation.domain.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 05/11/13
 * Time: 18:26
 */
@Component("validator-user")
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {
        if(!StringUtils.hasLength(((User) o).getMail())) { errors.rejectValue("mail", null, "validation.user.mail"); }
        if(!StringUtils.hasLength(((User) o).getPassword())) { errors.rejectValue("password", null, "validation.user.password"); }
    }
}
