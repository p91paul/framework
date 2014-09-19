package applica.framework.library.cells.renderers;

import applica.framework.Grid;
import applica.framework.GridColumn;
import org.springframework.stereotype.Component;

@Component
public class LinkCellRenderer extends BaseCellRenderer {
    protected String createTemplatePath(Grid grid, GridColumn column) {
        return "/templates/cells/text_link.vm";
    }
}
