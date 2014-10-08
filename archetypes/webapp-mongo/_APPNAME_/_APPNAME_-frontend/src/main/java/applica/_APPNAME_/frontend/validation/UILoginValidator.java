package applica._APPNAME_.frontend.validation;

import applica._APPNAME_.frontend.viewmodel.UILogin;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 26/10/13
 * Time: 12:41
 */
@Component("validator-" + UILogin.EID)
public class UILoginValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {
        UILogin login = (UILogin)o;

        if(!StringUtils.hasLength(login.getUsername())) { errors.rejectValue("username", null, "validation.login.username"); }
        if(!StringUtils.hasLength(login.getPassword())) { errors.rejectValue("password", null, "validation.login.password"); }
    }
}
