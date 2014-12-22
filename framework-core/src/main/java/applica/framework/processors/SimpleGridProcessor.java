package applica.framework.processors;

import applica.framework.Grid;
import applica.framework.GridProcessException;
import applica.framework.data.Entity;
import applica.framework.mapping.GridDataMapper;
import applica.framework.mapping.SimpleGridDataMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Paolo Inaudi
 */
public class SimpleGridProcessor implements GridProcessor {

    @Override
    public List<Map<String, Object>> toMap(Grid grid, List<? extends Entity> entities) throws GridProcessException {
        List<Map<String, Object>> data = new ArrayList<>();
        GridDataMapper mapper = getDataMapper();
        mapper.mapGridDataFromEntities(grid.getDescriptor(), data, entities);
        return data;
    }

    protected static SimpleGridDataMapper getDataMapper() {
        return new SimpleGridDataMapper();
    }

}
