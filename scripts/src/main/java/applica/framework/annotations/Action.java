package applica.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 01/10/14
 * Time: 19:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {

    String value();
    String description() default "";

}
