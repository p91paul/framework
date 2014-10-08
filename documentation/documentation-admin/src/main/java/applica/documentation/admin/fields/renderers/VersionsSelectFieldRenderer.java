package applica.documentation.admin.fields.renderers;

import applica.documentation.domain.data.VersionsRepository;
import applica.documentation.domain.model.Version;
import applica.framework.library.SimpleItem;
import applica.framework.library.fields.renderers.SelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 19:17
 */
@Component
public class VersionsSelectFieldRenderer extends SelectFieldRenderer {

    @Autowired
    private VersionsRepository versionsRepository;

    @Override
    public List<SimpleItem> getItems() {
        List<Version> versions = versionsRepository.find(null).getRows(Version.class);
        return SimpleItem.createList(versions, "name", "id");
    }

}
