package applica.framework.library.velocity;

import applica.framework.FormField;
import applica.framework.render.FormFieldRenderer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.util.StringUtils;

import java.io.Writer;

public class VelocityFormFieldRenderer extends VelocityRenderer implements FormFieldRenderer {

    public VelocityFormFieldRenderer() {
    }

    public VelocityFormFieldRenderer(String templatePath) {
        super();
        setTemplatePath(templatePath);
    }

    @Override
    public void render(Writer writer, FormField field, Object value) {
        if (!StringUtils.hasLength(getTemplatePath())) return;

        Template template = VelocityBuilderProvider.provide().engine().getTemplate(getTemplatePath(), "UTF-8");
        VelocityContext context = new VelocityContext();
        context.put("field", field);
        context.put("value", value != null ? value : "");

        setupContext(context);

        template.merge(context, writer);
    }

}
