package applica.documentation.domain.validation;

import applica.documentation.domain.model.Role;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 05/11/13
 * Time: 18:26
 */
@Component("validator-role")
public class RoleValidator implements Validator {
    
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {
        if(!StringUtils.hasLength(((Role) o).getRole())) { errors.rejectValue("role", null, "validation.role.role"); }
    }
}
