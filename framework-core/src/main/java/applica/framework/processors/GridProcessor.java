package applica.framework.processors;

import applica.framework.Grid;
import applica.framework.GridProcessException;
import applica.framework.data.Entity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Paolo Inaudi
 */
public interface GridProcessor {

    public List<Map<String, Object>> toMap(Grid grid, List<? extends Entity> entities) throws GridProcessException;

}
