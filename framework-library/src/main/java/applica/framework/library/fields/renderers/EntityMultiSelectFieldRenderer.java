package applica.framework.library.fields.renderers;

import applica.framework.data.Entity;
import applica.framework.data.LoadRequest;
import applica.framework.data.RepositoriesFactory;
import applica.framework.library.SelectableItem;
import applica.framework.library.SimpleItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 03/11/14
 * Time: 12:17
 */
public abstract class EntityMultiSelectFieldRenderer extends MultiSelectFieldRenderer {

    @Autowired
    private RepositoriesFactory repositoriesFactory;

    public abstract Class<? extends Entity> getEntityType();

    @Override
    public List<SelectableItem> getItems(List<? extends Entity> selectedItems) {
        return (List<SelectableItem>) repositoriesFactory.createForEntity(getEntityType())
                .find(LoadRequest.build()).getRows().stream()
                .map(e -> new SelectableItem(
                        e.toString(),
                        ((Entity) e).getId().toString(),
                        selectedItems.stream().anyMatch(ce -> ((Entity) ce).getId().equals(ce.getId()))))
                .collect(Collectors.toList());
    }

}
