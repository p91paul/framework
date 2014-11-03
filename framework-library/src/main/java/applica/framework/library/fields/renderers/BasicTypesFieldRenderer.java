package applica.framework.library.fields.renderers;

import applica.framework.Form;
import applica.framework.FormField;
import applica.framework.library.i18n.Localization;
import applica.framework.library.velocity.VelocityFormFieldRenderer;
import applica.framework.utils.TypeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;

public class BasicTypesFieldRenderer extends TemplateFormFieldRenderer {

    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        String templateType = TypeUtils.getRawClassFromGeneric(formField.getDataType()).getSimpleName().toLowerCase();
        String templateFile = null;

        switch (templateType) {
            case "integer":
            case "int":
            case "long":
                templateFile = "number";
                break;
            case "date":
                templateFile = "date";
                break;
            case "boolean":
                templateFile = "check";
                break;
            default:
                templateFile = "text";
                break;
        }

        return String.format("/templates/fields/%s.vm", templateFile);
    }
}
