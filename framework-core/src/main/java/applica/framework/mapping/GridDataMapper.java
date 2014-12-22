package applica.framework.mapping;

import applica.framework.GridDescriptor;
import applica.framework.data.Entity;
import java.util.List;
import java.util.Map;

public interface GridDataMapper {

    public List<Map<String, Object>> mapGridDataFromEntities(GridDescriptor gridDescriptor,
            List<? extends Entity> entities);
}
