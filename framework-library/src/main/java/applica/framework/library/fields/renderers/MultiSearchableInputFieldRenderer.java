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

@Component
public abstract class MultiSearchableInputFieldRenderer extends VelocityFormFieldRenderer {

    public abstract String getServiceUrl();

    public abstract List<SimpleItem> getSelectedItems(FormField field, Object value);

    @Override
    public void render(Writer writer, FormField field, Object value) {
        setTemplatePath("/templates/fields/multi_searchable_input.vm");
        List<SimpleItem> items = getSelectedItems(field, value);
        putExtraContextValue("selectedItems", items);

        super.render(writer, field, value);
    }

    @Override
    protected void setupContext(VelocityContext context) {
        super.setupContext(context);

        context.put("serviceUrl", getServiceUrl());
    }
}
