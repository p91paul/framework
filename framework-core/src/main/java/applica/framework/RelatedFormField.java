package applica.framework;

import applica.framework.data.Repository;
import applica.framework.render.FormFieldRenderer;

import java.lang.reflect.Type;

public class RelatedFormField extends FormField {
    private Repository repository;

    public RelatedFormField(Form form, String property, Type dataType, String description, String tooltip, FormFieldRenderer renderer, Repository repository) {
        super(form, property, dataType, description, tooltip, renderer);
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }


}
