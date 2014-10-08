package applica.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 4/18/13
 * Time: 9:32 AM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormButtons {
    FormButton[] value();
}
