package applica.framework.library.cells.renderers;

import applica.framework.Grid;
import applica.framework.GridColumn;
import applica.framework.library.i18n.Localization;
import applica.framework.library.velocity.VelocityCellRenderer;
import applica.framework.utils.TypeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;

public class BaseCellRenderer extends VelocityCellRenderer {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Override
    public void render(Writer writer, GridColumn column, Object value) {
        String templatePath = createTemplatePath(column.getGrid(), column);
        setTemplatePath(templatePath);

        logger.info("Loading form template: " + getTemplatePath());

        super.render(writer, column, value);
    }

    protected String createTemplatePath(Grid grid, GridColumn column) {
        String templateType = TypeUtils.getRawClassFromGeneric(column.getDataType()).getSimpleName().toLowerCase();
        String templateFile = null;

        switch (templateType) {
            case "integer":
            case "int":
            case "long":
                templateFile = "number";
                break;
            case "date":
                templateFile = "date";
                break;
            case "boolean":
                templateFile = "check";
                break;
            default:
                templateFile = "text";
                break;
        }

        return String.format("/templates/cells/%s.vm", templateFile);
    }

    @Override
    protected void setupContext(VelocityContext context) {
        super.setupContext(context);

        Localization localization = new Localization(webApplicationContext);
        context.put("localization", localization);
        context.put("wwwBase", webApplicationContext.getServletContext().getContextPath());
    }


}
