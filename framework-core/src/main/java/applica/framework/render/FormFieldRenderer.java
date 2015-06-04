package applica.framework.render;

import applica.framework.FormField;
import applica.framework.data.Entity;
import java.io.Writer;

public interface FormFieldRenderer<V, T extends Entity> {

    void render(Writer writer, FormField field, V value);

    default void render(Writer writer, FormField field, V value, T rendered) {
        render(writer, field, value);
    }
}
