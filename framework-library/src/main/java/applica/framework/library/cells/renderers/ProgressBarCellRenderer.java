package applica.framework.library.cells.renderers;

import applica.framework.Grid;
import applica.framework.GridColumn;
import org.springframework.stereotype.Component;

public class ProgressBarCellRenderer extends BaseCellRenderer {

    @Override
    protected String createTemplatePath(Grid grid, GridColumn column) {
        return "/templates/cells/progress.vm";
    }

}
