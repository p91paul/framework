package applica._APPNAME_.admin.fields.renderers;

import applica.framework.library.fields.renderers.ImagesFieldRenderer;
import org.springframework.stereotype.Component;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 07/03/14
 * Time: 18:29
 */
@Component
public class UserImageFieldRenderer extends ImagesFieldRenderer {

    @Override
    public String getPath() {
        return "users/";
    }
}
