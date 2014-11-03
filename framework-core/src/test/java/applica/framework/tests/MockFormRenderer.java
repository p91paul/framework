package applica.framework.tests;

import applica.framework.Form;
import applica.framework.FormField;
import applica.framework.render.FormRenderer;

import java.io.Writer;
import java.util.Map;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 03/11/14
 * Time: 11:14
 */
public class MockFormRenderer implements FormRenderer {
    @Override
    public void render(Writer writer, Form form, Map<String, Object> data) {
        for (FormField formField : form.getDescriptor().getFields()) {
            formField.write(writer, data);
        }
    }
}
