package applica._APPNAME_.admin.fields.renderers;

import applica.framework.FormField;
import applica.framework.library.fields.renderers.SingleSearchableInputFieldRenderer;
import org.springframework.stereotype.Component;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 3/4/13
 * Time: 11:23 AM
 */
@Component
public class LibraryDemoSingleSearchableInputRenderer extends SingleSearchableInputFieldRenderer {

    @Override
    public String getLabel(FormField field, Object value) {
        return String.valueOf(value);
    }

    @Override
    public String getServiceUrl() {
        return "values/roles";
    }

}
