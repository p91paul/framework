package applica.framework.library.cells.renderers;

import applica.framework.Grid;
import applica.framework.GridColumn;
import org.springframework.stereotype.Component;

@Component
public class ColorCellRenderer extends BaseCellRenderer {

    @Override
    protected String createTemplatePath(Grid grid, GridColumn column) {
        return "/templates/cells/color.vm";
    }

}
