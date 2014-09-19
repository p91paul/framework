package applica.documentation.admin.fields.renderers;

import applica.framework.Form;
import applica.framework.FormField;
import applica.framework.library.fields.renderers.BaseFieldRenderer;
import org.springframework.stereotype.Component;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 19:58
 */
@Component
public class HtmlFieldRenderer extends BaseFieldRenderer {

    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        return "/templates/fields/custom/html.vm";
    }

}
