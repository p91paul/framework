package applica.framework.library.fields.renderers;

import applica.framework.FormField;
import applica.framework.library.SelectableItem;
import applica.framework.library.i18n.Localization;
import applica.framework.library.velocity.VelocityFormFieldRenderer;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;
import java.util.List;

public abstract class CheckboxesFieldRenderer extends VelocityFormFieldRenderer {

    public abstract List<SelectableItem> getItems(FormField field, Object value);

    @Override
    public void render(Writer writer, FormField field, Object value) {
        setTemplatePath("/templates/fields/checkboxes.vm");
        putExtraContextValue("items", getItems(field, value));

        super.render(writer, field, value);
    }
}
