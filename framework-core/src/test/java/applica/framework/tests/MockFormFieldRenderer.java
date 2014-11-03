package applica.framework.tests;

import applica.framework.FormField;
import applica.framework.render.FormFieldRenderer;
import applica.framework.utils.TypeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 03/11/14
 * Time: 11:13
 */
public class MockFormFieldRenderer implements FormFieldRenderer {
    @Override
    public void render(Writer writer, FormField field, Object value) {
        try {
            if (TypeUtils.isList(value.getClass())) {
                value = StringUtils.join(((List) value), ", ");
            }
            writer.write(String.format("%s = %s\n", field.getProperty(), value != null ? value.toString() : "NA"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
