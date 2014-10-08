package applica.framework.builders;

import applica.framework.CrudConfiguration;
import applica.framework.data.Entity;
import applica.framework.data.Repository;
import applica.framework.mapping.PropertyMapper;
import applica.framework.processors.FormProcessor;
import applica.framework.render.FormFieldRenderer;
import applica.framework.render.FormRenderer;
import applica.framework.utils.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 3/14/13
 * Time: 4:45 PM
 */
public class FormConfigurator {
    private Class<? extends Entity> entityType;
    private String identifier;

    private FormConfigurator() {

    }

    public static FormConfigurator build(Class<? extends Entity> entityType, String identifier) {
        FormConfigurator formConfigurator = new FormConfigurator();
        formConfigurator.entityType = entityType;
        formConfigurator.identifier = identifier;

        CrudConfiguration.instance().registerForm(entityType, identifier);

        return formConfigurator;
    }

    public FormConfigurator renderer(Class<? extends FormRenderer> renderer) {
        CrudConfiguration.instance().registerFormRenderer(entityType, renderer);
        return this;
    }

    public FormConfigurator processor(Class<? extends FormProcessor> formProcessor) {
        CrudConfiguration.instance().registerFormProcessor(entityType, formProcessor);
        return this;
    }

    public FormConfigurator repository(Class<? extends Repository> repository) {
        CrudConfiguration.instance().registerFormRepository(entityType, repository);
        return this;
    }

    public FormConfigurator method(String method) {
        CrudConfiguration.instance().registerFormMethod(entityType, method);
        return this;
    }

    public FormConfigurator button(String label, String type, String action) {
        CrudConfiguration.instance().registerFormButton(entityType, label, type, action);
        return this;
    }

    public FormConfigurator field(String property, String description) {
        CrudConfiguration.instance().registerFormField(entityType, property, getDataType(property), description, "");
        return this;
    }

    public FormConfigurator field(String property, String description, Class<? extends FormFieldRenderer> renderer) {
        CrudConfiguration.instance().registerFormField(entityType, property, getDataType(property), description, "");
        CrudConfiguration.instance().registerFormFieldRenderer(entityType, property, renderer);
        return this;
    }

    public FormConfigurator field(String property, String description, Class<? extends FormFieldRenderer> renderer, Class<? extends PropertyMapper> propertyMapper) {
        CrudConfiguration.instance().registerFormField(entityType, property, getDataType(property), description, "");
        CrudConfiguration.instance().registerFormFieldRenderer(entityType, property, renderer);
        CrudConfiguration.instance().registerPropertyMapper(entityType, property, propertyMapper);
        return this;
    }

    public FormConfigurator propertyMapper(String property, Class<? extends PropertyMapper> propertyMapper) {
        CrudConfiguration.instance().registerPropertyMapper(entityType, property, propertyMapper);
        return this;
    }

    public FormConfigurator relatedField(String property, String description, String tooltip, Repository repository) {
        CrudConfiguration.instance().registerRelatedFormField(entityType, property, getDataType(property), description, tooltip, repository);
        return this;
    }

    public FormConfigurator relatedField(String property, String description, String tooltip, Repository repository, Class<? extends FormFieldRenderer> renderer) {
        CrudConfiguration.instance().registerRelatedFormField(entityType, property, getDataType(property), description, tooltip, repository);
        CrudConfiguration.instance().registerFormFieldRenderer(entityType, property, renderer);
        return this;
    }

    public FormConfigurator param(String property, String key, String value) {
        CrudConfiguration.instance().setPropertyParam(entityType, property, key, value);
        return this;
    }

    //


    private Type getDataType(String property) {
        if(entityType != null) {
            try {
                Field field = TypeUtils.getField(entityType, property);
                return field.getGenericType();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e); //programmers error
            }
        }

        return null;
    }
}
