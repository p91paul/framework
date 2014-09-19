package applica.framework.library;

import applica.framework.CrudFactory;
import applica.framework.data.Entity;
import applica.framework.data.Repository;
import applica.framework.mapping.PropertyMapper;
import applica.framework.processors.FormProcessor;
import applica.framework.render.CellRenderer;
import applica.framework.render.FormFieldRenderer;
import applica.framework.render.FormRenderer;
import applica.framework.render.GridRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultCrudFactory implements CrudFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public FormFieldRenderer createFormFieldRenderer(
            Class<? extends FormFieldRenderer> type,
            Class<? extends Entity> entityType, String identifier,
            String property) {
        return applicationContext.getBean(type);
    }

    @Override
    public FormRenderer createFormRenderer(Class<? extends FormRenderer> type,
                                           Class<? extends Entity> entityType, String identifier) {
        return applicationContext.getBean(type);
    }

    @Override
    public FormProcessor createFormProcessor(
            Class<? extends FormProcessor> type,
            Class<? extends Entity> entityType, String identifier) {
        return applicationContext.getBean(type);
    }

    @Override
    public GridRenderer createGridRenderer(Class<? extends GridRenderer> type,
                                           Class<? extends Entity> entityType, String identifier) {
        return applicationContext.getBean(type);
    }

    @Override
    public CellRenderer createCellRenderer(Class<? extends CellRenderer> type,
                                           Class<? extends Entity> entityType, String identifier,
                                           String property) {
        return applicationContext.getBean(type);
    }

    @Override
    public Repository createRepository(Class<? extends Repository> type,
                                       Class<? extends Entity> entityType, String identifier) {
        if (type != null)
            return applicationContext.getBean(type);
        else
            return (Repository) applicationContext.getBean(String.format("repository-%s", identifier));
    }

    @Override
    public PropertyMapper createPropertyMapper(Class<? extends PropertyMapper> type,
                                               Class<? extends Entity> entityType,
                                               String identifier,
                                               String property) {
        return applicationContext.getBean(type);
    }

}
