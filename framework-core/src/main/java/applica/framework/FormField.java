package applica.framework;

import applica.framework.render.FormFieldRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a form field. The data in this object is used for rendering and mapping
 */
public class FormField {
    String description;
    String tooltip;
    String property;
    String fieldSet;
    String searchCriteria;
    FormFieldRenderer renderer;
    Form form;
    Type dataType;
    Map<String, String> params = new HashMap<>();

    Log logger = LogFactory.getLog(getClass());

    public FormField(Form form, String property, Type dataType, String description, String tooltip, String fieldSet, FormFieldRenderer renderer) {
        super();
        this.form = form;
        this.property = property;
        this.fieldSet = fieldSet;
        this.dataType = dataType;
        this.description = description;
        this.tooltip = tooltip;
        this.renderer = renderer;
    }

    public FormField() {
        super();
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getFieldSet() {
        return fieldSet;
    }

    public void setFieldSet(String fieldSet) {
        this.fieldSet = fieldSet;
    }

    public FormFieldRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(FormFieldRenderer renderer) {
        this.renderer = renderer;
    }

    public Type getDataType() {
        return dataType;
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String writeToString(Map<String, Object> data) {
        StringWriter writer = new StringWriter();
        write(writer, data);

        return writer.toString();
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public void write(Writer writer, Map<String, Object> data) {
        Object value = null;
        if(data != null) {
            if (data.containsKey(property)) value = data.get(property);
        }

        try {
            if (renderer == null) {
                writer.write("ERROR");
                logger.warn("Renderer not found for " + property);
                return;
            }

            renderer.render(writer, this, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error rendering field " + property + ": " + ex.getMessage());
        }
    }
}
