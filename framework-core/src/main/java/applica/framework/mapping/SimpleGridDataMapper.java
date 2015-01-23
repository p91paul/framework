package applica.framework.mapping;

import applica.framework.GridDescriptor;
import applica.framework.data.Entity;
import applica.framework.utils.PropertyPathUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleGridDataMapper implements GridDataMapper {

    @Override
    public List<Map<String, Object>> mapGridDataFromEntities(GridDescriptor gridDescriptor,
            List<? extends Entity> entities) {
        return entities.stream().map(e -> mapRow(gridDescriptor, e)).collect(Collectors.toList());
    }

    protected <T extends Entity> Map<String, Object> mapRow(GridDescriptor gridDescriptor, T entity) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", entity.getId());
        gridDescriptor.getColumns().stream()
                .forEach(column -> {
                    final String property = column.getProperty();
                    row.put(property, mapColumn(entity, column.getProperty()));
                });
        return row;
    }

    protected <T extends Entity> Object mapColumn(T entity, String property) {
        try {
            return PropertyPathUtils.getPropertyFromPath(entity, property);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
