package applica.framework.library.fields.renderers;

import applica.framework.FormField;
import applica.framework.library.SimpleItem;
import applica.framework.library.i18n.Localization;
import applica.framework.library.velocity.VelocityFormFieldRenderer;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;
import java.util.List;

public abstract class SelectFieldRenderer extends VelocityFormFieldRenderer {

    public abstract List<SimpleItem> getItems();

    @Override
    public void render(Writer writer, FormField field, Object value) {
        setTemplatePath("/templates/fields/select.vm");

        super.render(writer, field, value);
    }

    @Override
    protected void setupContext(VelocityContext context) {
        super.setupContext(context);

        context.put("items", getItems());
    }
}
