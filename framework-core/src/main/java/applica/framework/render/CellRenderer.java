package applica.framework.render;

import applica.framework.GridColumn;

import java.io.Writer;

/**
 * Represents the base interface for cells rendering
 */
public interface CellRenderer {

    /**
     * Render a cell in specified writer
     * @param writer The writer that contains output
     * @param column Column object that contains information about column (header, type, ...)
     * @param value Cell value
     */
    void render(Writer writer, GridColumn column, Object value);
}
