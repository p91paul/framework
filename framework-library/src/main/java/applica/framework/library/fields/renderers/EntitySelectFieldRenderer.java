package applica.framework.library.fields.renderers;

import applica.framework.data.Entity;
import applica.framework.data.LoadRequest;
import applica.framework.data.RepositoriesFactory;
import applica.framework.library.SimpleItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 03/11/14
 * Time: 12:11
 */
public class EntitySelectFieldRenderer extends SelectFieldRenderer {

    @Autowired
    private RepositoriesFactory repositoriesFactory;

    private Class<? extends Entity> entityType;

    public Class<? extends Entity> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<? extends Entity> entityType) {
        this.entityType = entityType;
    }

    @Override
    public List<SimpleItem> getItems() {
        return (List<SimpleItem>) repositoriesFactory.createForEntity(getEntityType())
                .find(LoadRequest.build()).getRows().stream()
                .map(e -> new SimpleItem(e.toString(), ((Entity) e).getId().toString()))
                .collect(Collectors.toList());
    }
}
