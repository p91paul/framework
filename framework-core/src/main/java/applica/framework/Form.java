package applica.framework;

import applica.framework.builders.FormBuilder;
import applica.framework.builders.FormProcessorBuilder;
import applica.framework.data.Entity;
import applica.framework.processors.FormProcessor;
import applica.framework.render.FormRenderer;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Form {
    String identifier;
    FormDescriptor descriptor;
    FormRenderer renderer;
    boolean editMode = false;
    private String method = "POST";
    private Map<String, Object> data;
    private List<FieldSet> fieldSets;

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

    public FormDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(FormDescriptor descriptor) {
        this.descriptor = descriptor;

        //adjust references
        if (descriptor != null) {
            descriptor.setForm(this);
        }
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

    public List<FieldSet> getFieldSets() {
        if (fieldSets == null) {
            fieldSets = new ArrayList<>();

            FieldSet defaultFieldSet = new FieldSet(this);
            defaultFieldSet.setName("<default>");
            defaultFieldSet.setFields(
                    descriptor.getFields().stream().filter(f -> StringUtils.isEmpty(f.getFieldSet())).collect(Collectors.toList())
            );
            fieldSets.add(defaultFieldSet);

            descriptor.getFields()
                    .stream()
                    .filter(f -> StringUtils.isNotEmpty(f.getFieldSet()))
                    .map(FormField::getFieldSet)
                    .distinct()
                    .forEach(fs -> {
                        FieldSet fieldSet = new FieldSet(this);
                        fieldSet.setName(fs);
                        fieldSet.setFields(
                                descriptor.getFields().stream().filter(f -> fs.equals(f.getFieldSet())).collect(Collectors.toList())
                        );
                        fieldSets.add(fieldSet);
                    });

        }

        return fieldSets;

    }

    public String writeToString() throws FormCreationException, CrudConfigurationException {
        StringWriter writer = new StringWriter();
        write(writer);

        return writer.toString();
    }

    public void write(Writer writer) throws FormCreationException, CrudConfigurationException {
        if (descriptor == null) throw new FormCreationException("Missing descriptor");
        if (renderer == null) throw new FormCreationException("Missing renderer");

        renderer.render(writer, this, data);
    }
}
