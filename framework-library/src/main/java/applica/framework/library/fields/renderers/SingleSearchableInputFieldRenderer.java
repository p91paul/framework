package applica.framework.library.fields.renderers;

import applica.framework.FormField;
import applica.framework.library.i18n.Localization;
import applica.framework.library.velocity.VelocityFormFieldRenderer;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;

public abstract class SingleSearchableInputFieldRenderer extends VelocityFormFieldRenderer {

    public abstract String getLabel(FormField field, Object value);

    public abstract String getServiceUrl();

    @Override
    public void render(Writer writer, FormField field, Object value) {
        putExtraContextValue("label", getLabel(field, value));

        super.render(writer, field, value);
    }

    @Override
    protected void setupContext(VelocityContext context) {
        super.setupContext(context);

        context.put("serviceUrl", getServiceUrl());
    }

    @Override
    public String getTemplatePath() {
        return "/templates/fields/single_searchable_input.vm";
    }
}
