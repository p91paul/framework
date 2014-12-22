package applica.framework;

import applica.framework.data.Entity;
import applica.framework.render.FormRenderer;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class Form {

    private String identifier;
    private Class<? extends Entity> entityType;
    private FormDescriptor descriptor;
    private FormRenderer renderer;
    boolean editMode = false;
    private String method = "POST";
    private Map<String, Object> data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Class<? extends Entity> getEntityType() {
        return entityType;
    }

    public void setEntityType(
            Class<? extends Entity> entityType) {
        this.entityType = entityType;
    }

    public FormDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(FormDescriptor descriptor) {
        this.descriptor = descriptor;

        //adjust references
        if (descriptor != null)
            descriptor.setForm(this);
    }

    public FormRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(FormRenderer renderer) {
        this.renderer = renderer;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public String writeToString() throws FormCreationException, CrudConfigurationException {
        StringWriter writer = new StringWriter();
        write(writer);

        return writer.toString();
    }

    public void write(Writer writer) throws FormCreationException, CrudConfigurationException {
        if (descriptor == null)
            throw new FormCreationException("Missing descriptor");
        if (renderer == null)
            throw new FormCreationException("Missing renderer");

        renderer.render(writer, this, data);
    }
}
