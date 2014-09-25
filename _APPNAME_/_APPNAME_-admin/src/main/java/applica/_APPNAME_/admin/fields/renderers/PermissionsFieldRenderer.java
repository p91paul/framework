package applica._APPNAME_.admin.fields.renderers;

import applica.framework.FormField;
import applica.framework.library.SimpleItem;
import applica.framework.library.fields.renderers.MultiSearchableInputFieldRenderer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 25/09/14
 * Time: 16:45
 */
@Component
public class PermissionsFieldRenderer extends MultiSearchableInputFieldRenderer {

    @Override
    public String getServiceUrl() {
        return "values/permissions";
    }

    @Override
    public List<SimpleItem> getSelectedItems(FormField formField, Object o) {
        return null;
    }
}
