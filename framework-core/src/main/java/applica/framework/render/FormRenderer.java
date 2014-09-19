package applica.framework.render;

import applica.framework.Form;

import java.io.Writer;
import java.util.Map;

public interface FormRenderer {
    void render(Writer writer, Form form, Map<String, Object> data);
}
