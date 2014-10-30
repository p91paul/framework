package applica.framework;

import applica.framework.render.FormFieldRenderer;

import java.lang.reflect.Type;

public class RelatedFormField extends FormField {

    public RelatedFormField(Form form, String property, Type dataType, String description, String tooltip, FormFieldRenderer renderer) {
        super(form, property, dataType, description, tooltip, renderer);
    }

    public RelatedFormField() {
    }

}
