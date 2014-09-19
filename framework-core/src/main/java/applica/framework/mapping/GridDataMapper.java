package applica.framework.mapping;

import applica.framework.GridDescriptor;
import applica.framework.data.Entity;

import java.util.List;
import java.util.Map;

public interface GridDataMapper {
    void mapGridDataFromEntities(GridDescriptor gridDescriptor, List<Map<String, Object>> gridData, List<? extends Entity> entities);
}
