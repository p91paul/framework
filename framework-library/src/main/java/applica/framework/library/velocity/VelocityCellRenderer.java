package applica.framework.library.velocity;

import applica.framework.GridColumn;
import applica.framework.render.CellRenderer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.util.StringUtils;

import java.io.Writer;

public class VelocityCellRenderer extends VelocityRenderer implements CellRenderer {

    public VelocityCellRenderer() {
    }

    public VelocityCellRenderer(String templatePath) {
        super();
        setTemplatePath(templatePath);
    }

    @Override
    public void render(Writer writer, GridColumn column, Object value) {
        if (!StringUtils.hasLength(getTemplatePath())) return;

        Template template = VelocityBuilderProvider.provide().engine().getTemplate(getTemplatePath(), "UTF-8");
        VelocityContext context = new VelocityContext();
        context.put("column", column);
        context.put("value", value != null ? value : "");

        setupContext(context);

        template.merge(context, writer);
    }

}
