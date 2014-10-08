package applica.framework;

import applica.framework.data.Entity;
import applica.framework.data.Repository;
import applica.framework.mapping.PropertyMapper;
import applica.framework.processors.FormProcessor;
import applica.framework.render.CellRenderer;
import applica.framework.render.FormFieldRenderer;
import applica.framework.render.FormRenderer;
import applica.framework.render.GridRenderer;

public interface CrudFactory {
    FormFieldRenderer createFormFieldRenderer(Class<? extends FormFieldRenderer> type, Class<? extends Entity> entityType, String identifier, String property);

    FormRenderer createFormRenderer(Class<? extends FormRenderer> type, Class<? extends Entity> entityType, String identifier);

    FormProcessor createFormProcessor(Class<? extends FormProcessor> type, Class<? extends Entity> entityType, String identifier);

    GridRenderer createGridRenderer(Class<? extends GridRenderer> type, Class<? extends Entity> entityType, String identifier);

    CellRenderer createCellRenderer(Class<? extends CellRenderer> type, Class<? extends Entity> entityType, String identifier, String property);

    Repository createRepository(Class<? extends Repository> type, Class<? extends Entity> entityType);

    PropertyMapper createPropertyMapper(Class<? extends PropertyMapper> type, Class<? extends Entity> entityType, String identifier, String property);
}
