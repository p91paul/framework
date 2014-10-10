package applica.framework.annotations;

import applica.framework.data.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelatedFormField {
    String description() default "";

    String tooltip() default "";

    Class<? extends applica.framework.data.Repository> repository() default applica.framework.data.Repository.class;
}
