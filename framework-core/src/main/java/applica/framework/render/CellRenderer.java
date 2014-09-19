package applica.framework.render;

import applica.framework.GridColumn;

import java.io.Writer;

public interface CellRenderer {
    void render(Writer writer, GridColumn column, Object value);
}
