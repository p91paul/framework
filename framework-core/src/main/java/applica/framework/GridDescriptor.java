package applica.framework;

import applica.framework.render.CellRenderer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GridDescriptor {
    private Grid grid;
    private List<GridColumn> columns = new ArrayList<GridColumn>();

    public GridDescriptor(Grid grid) {
        super();
        this.grid = grid;

        if(grid != null) {
            grid.setDescriptor(this);
        }
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;

        for (GridColumn column : getColumns()) {
            column.setGrid(grid);
        }
    }

    public List<GridColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<GridColumn> columns) {
        this.columns = columns;
    }

    public GridColumn addColumn(String property, String header, Type dataType, boolean linked, CellRenderer renderer) {
        GridColumn column = new GridColumn(grid, property, header, dataType, linked, renderer);
        columns.add(column);

        return column;
    }
}
