package applica.framework.library.fields.renderers;

import applica.framework.Form;
import applica.framework.FormField;

public class HiddenFieldRenderer extends TemplateFormFieldRenderer {

    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        return "/templates/fields/hidden.vm";
    }

}
