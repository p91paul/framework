package applica.framework.library.cells.renderers;

import applica.framework.GridColumn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.text.MessageFormat;

@Component
public class PercentageCellRenderer extends BaseCellRenderer {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public void render(Writer writer, GridColumn column, Object value) {
        if (value != null) {
            value = MessageFormat.format("{0}%", value);
        } else {
            logger.trace("value is null");
        }
        super.render(writer, column, value);
    }
}
