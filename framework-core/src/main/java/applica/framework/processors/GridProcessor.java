package applica.framework.processors;

import applica.framework.FormProcessException;
import applica.framework.Grid;
import applica.framework.data.Entity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Paolo Inaudi
 */
public interface GridProcessor {

    public List<Map<String, Object>> toMap(Grid grid, List<Entity> entities) throws FormProcessException;

}
