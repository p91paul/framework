package applica.documentation.admin.fields.renderers;

import applica.documentation.domain.model.Role;
import applica.framework.FormField;
import applica.framework.library.SimpleItem;
import applica.framework.library.fields.renderers.MultiSearchableInputFieldRenderer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 2/26/13
 * Time: 6:09 PM
 */
@Component
public class RolesFieldRenderer extends MultiSearchableInputFieldRenderer {

    @Override
    public String getServiceUrl() {
        return "values/roles";
    }

    @Override
    public List<SimpleItem> getSelectedItems(FormField field, Object value) {
        List<Role> roles = (List<Role>) value;
        if(roles == null) {
            roles = new ArrayList<>();
        }

        return SimpleItem.createList(roles, "role", "role");
    }
}
