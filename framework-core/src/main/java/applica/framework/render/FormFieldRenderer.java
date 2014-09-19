package applica.framework.render;

import applica.framework.FormField;

import java.io.Writer;

public interface FormFieldRenderer {
    void render(Writer writer, FormField field, Object value);
}
