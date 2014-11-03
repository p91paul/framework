package applica.framework.library.fields.renderers;

import applica.framework.Form;
import applica.framework.FormField;

public class ColorFieldRenderer extends BasicTypesFieldRenderer {

    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        return "/templates/fields/color.vm";
    }

}
