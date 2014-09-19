package applica.framework.library.cells.renderers;

import applica.framework.Grid;
import applica.framework.GridColumn;
import org.springframework.stereotype.Component;

@Component
public class UlCellRenderer extends BaseCellRenderer {
    protected String createTemplatePath(Grid grid, GridColumn column) {
        return "/templates/cells/ul.vm";
    }
}
