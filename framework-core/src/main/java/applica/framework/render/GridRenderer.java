package applica.framework.render;

import applica.framework.Grid;

import java.io.Writer;
import java.util.List;
import java.util.Map;

public interface GridRenderer {
    void render(Writer writer, Grid grid, List<Map<String, Object>> rows);
}
