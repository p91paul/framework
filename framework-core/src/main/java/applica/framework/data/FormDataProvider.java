package applica.framework.data;

import applica.framework.CrudConfigurationException;
import applica.framework.Form;
import applica.framework.FormProcessException;
import applica.framework.processors.FormProcessor;

import java.util.Map;

public class FormDataProvider {
    private Repository repository;
    private FormProcessor formProcessor;

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public FormProcessor getFormProcessor() {
        return formProcessor;
    }

    public void setFormProcessor(FormProcessor formProcessor) {
        this.formProcessor = formProcessor;
    }

    public void load(Form form, Object entityId) throws CrudConfigurationException, FormProcessException {
        if (formProcessor == null) throw new CrudConfigurationException("Missing form processor");
        if (repository == null) throw new CrudConfigurationException("Missing repository");

        form.setEditMode(entityId != null);

        Entity entity = null;
        if (entityId != null) {
            entity = ((Entity) repository.get(entityId).orElseGet(() -> null));
        }

        Map<String, Object> data = formProcessor.toMap(form, entity);

        form.setData(data);
    }
}
