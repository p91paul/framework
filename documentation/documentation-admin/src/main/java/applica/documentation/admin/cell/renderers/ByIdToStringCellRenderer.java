package applica.documentation.admin.cell.renderers;

import applica.framework.ApplicationContextProvider;
import applica.framework.GridColumn;
import applica.framework.data.Entity;
import applica.framework.data.Repository;
import applica.framework.ApplicationContextProvider;
import applica.framework.library.cells.renderers.BaseCellRenderer;
import org.springframework.stereotype.Component;

import java.io.Writer;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 19:34
 */
@Component
public class ByIdToStringCellRenderer extends BaseCellRenderer {

    @Override
    public void render(Writer writer, GridColumn column, Object value) {
        String repositoryId = column.getParams().get("repository");
        Repository repository = (Repository) ApplicationContextProvider.provide().getBean(repositoryId);
        String finalValue = "NA";
        if (value != null) {
            Entity entity = repository.get(value);
            if (entity != null) {
                finalValue = entity.toString();
            }
        }

        super.render(writer, column, finalValue);
    }
}
