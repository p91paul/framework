package applica.framework.library.velocity;

import applica.framework.Grid;
import applica.framework.GridColumn;
import applica.framework.render.GridRenderer;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.util.StringUtils;

public class VelocityGridRenderer extends VelocityRenderer implements GridRenderer {

    public VelocityGridRenderer() {
        super();
    }

    public VelocityGridRenderer(String templatePath) {
        super();
        setTemplatePath(templatePath);
    }

    @Override
    public void render(Writer writer, Grid grid, List<Map<String, Object>> rows) {
        if (!StringUtils.hasLength(getTemplatePath()))
            return;

        Template template = VelocityBuilderProvider.provide().engine().getTemplate(getTemplatePath(), "UTF-8");
        VelocityContext context = new VelocityContext();
        context.put("grid", grid);
        context.put("identifier", grid.getIdentifier());
        context.put("columns", getColumns(grid));
        context.put("rows", rows);
        context.put("totalRows", rows.size());

        if (grid.getSearchForm() != null) {
            String renderedForm = "";
            try {
                renderedForm = grid.getSearchForm().writeToString();
            } catch (Exception e) {
                renderedForm = "Error rendering form: " + e.getMessage();
                e.printStackTrace();
            }

            context.put("searchForm", renderedForm);
        }

        setupContext(context);

        template.merge(context, writer);
    }

    protected List<GridColumn> getColumns(Grid grid) {
        return grid.getDescriptor().getColumns();
    }

}
