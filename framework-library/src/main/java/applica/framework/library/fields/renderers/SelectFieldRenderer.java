package applica.framework.library.fields.renderers;

import applica.framework.CrudConfiguration;
import applica.framework.FormField;
import applica.framework.library.SimpleItem;
import applica.framework.library.velocity.VelocityFormFieldRenderer;
import java.io.Writer;
import java.util.List;
import org.apache.velocity.VelocityContext;

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

    protected SimpleItem getSimpleItem(Class<?> selectClass, String selectValue) {
        return new SimpleItem(CrudConfiguration.getDefaultDescription(selectClass, selectValue), selectValue);
    }

    protected SimpleItem getSimpleItem(String selectIdentifier, String selectValue) {
        return new SimpleItem(CrudConfiguration.getDefaultDescription(selectIdentifier, selectValue), selectValue);
    }
}
