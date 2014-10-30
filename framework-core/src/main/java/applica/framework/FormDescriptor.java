package applica.framework;

import applica.framework.data.Repository;
import applica.framework.render.FormFieldRenderer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FormDescriptor {
    private Form form;
    private List<FormField> fields = new ArrayList<>();
    private List<FormButton> buttons = new ArrayList<>();

    public FormDescriptor(Form form) {
        super();
        this.form = form;

        if(form != null) {
            form.setDescriptor(this);
        }
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;

        for (FormField field : getFields()) {
            field.setForm(form);
        }
    }

    public List<FormField> getFields() {
        return fields;
    }

    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }

    public List<FormButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<FormButton> buttons) {
        this.buttons = buttons;
    }

    public FormField addField(String property, Type dataType, String description, String tooltip, FormFieldRenderer renderer) {
        FormField newField = new FormField(form, property, dataType, description, tooltip, renderer);
        fields.add(newField);
        return newField;
    }

    public RelatedFormField addRelatedField(String property, Type dataType, String description, String tooltip, FormFieldRenderer renderer) {
        RelatedFormField newField = new RelatedFormField(form, property, dataType, description, tooltip, renderer);
        fields.add(newField);
        return newField;
    }

    public FormButton addButton(String label, String type, String action) {
        FormButton button = new FormButton(label, type, action);
        buttons.add(button);
        return button;
    }
}
