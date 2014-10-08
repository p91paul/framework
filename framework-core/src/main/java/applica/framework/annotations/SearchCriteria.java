package applica.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 07/11/13
 * Time: 11:15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchCriteria {
    String value();
}