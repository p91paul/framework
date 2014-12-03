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
 * Applica (www.applicadoit.com) User: bimbobruno Date: 3/14/13 Time: 4:45 PM
 */
public class FormConfigurator {

    private Class<? extends Entity> entityType;
    private String identifier;

    private FormConfigurator() {

    }

    /**
     * build() method is deprecated. Use configure instead
     *
     * @param entityType
     * @param identifier
     * @return
     */
    @Deprecated
    public static FormConfigurator build(Class<? extends Entity> entityType, String identifier) {
        FormConfigurator formConfigurator = new FormConfigurator();
        formConfigurator.entityType = entityType;
        formConfigurator.identifier = identifier;

        CrudConfiguration.instance().registerForm(entityType, identifier);

        return formConfigurator;
    }

    /**
     * Configures a new form for {@code entityType} with identifier
     * {@code entityType.getSimpleName()}. See {@link #configure(java.lang.Class, java.lang.String)
     * }
     *
     * @param entityType
     * @return
     */
    public static FormConfigurator configure(Class<? extends Entity> entityType) {
        return configure(entityType, entityType.getSimpleName());
    }

    public static FormConfigurator configure(Class<? extends Entity> entityType, String identifier) {
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

    public FormConfigurator submitButton(String label) {
        return button(label, "submit", null);
    }

    public FormConfigurator button(String label, String type, String action) {
        CrudConfiguration.instance().registerFormButton(entityType, label, type, action);
        return this;
    }

    /**
     * Adds a new property to the form with label "label.identifier.property"
     * where identifier is the form identifier specified in
     * {@link #configure(java.lang.Class, java.lang.String)}.
     *
     * @param property the property name
     * @return the updated FormConfigurator
     */
    public FormConfigurator field(String property) {
        return field(property, null, null, null);
    }

    /**
     * Adds a new property to the form with label "label.identifier.property"
     * where identifier is the form identifier specified in
     * {@link #configure(java.lang.Class, java.lang.String)}.
     *
     * @param property the property name
     * @param renderer the renderer for this property
     * @return the updated FormConfigurator
     */
    public FormConfigurator field(String property, Class<? extends FormFieldRenderer> renderer) {
        return field(property, null, renderer);
    }

    /**
     * Adds a new property to the form with label "label.identifier.property"
     * where identifier is the form identifier specified in
     * {@link #configure(java.lang.Class, java.lang.String)}.
     *
     * @param property the property name
     * @param renderer the renderer for this property
     * @param propertyMapper the value mapper for this property
     * @return the updated FormConfigurator
     */
    public FormConfigurator field(String property, Class<? extends FormFieldRenderer> renderer, Class<? extends PropertyMapper> propertyMapper) {
        return field(property, null, renderer, propertyMapper);
    }

    public FormConfigurator field(String property, String description) {
        return field(property, description, null);
    }

    public FormConfigurator field(String property, String description, Class<? extends FormFieldRenderer> renderer) {
        return field(property, description, renderer, null);
    }

    public FormConfigurator field(String property, String description, Class<? extends FormFieldRenderer> renderer, Class<? extends PropertyMapper> propertyMapper) {
        if (description == null)
            description = CrudConfiguration.getDefaultDescription(identifier, property);
        CrudConfiguration.instance().registerFormField(entityType, property,
                getDataType(property), description, "");

        if (renderer != null)
            CrudConfiguration.instance().registerFormFieldRenderer(entityType, property, renderer);

        if (propertyMapper != null)
            CrudConfiguration.instance().registerPropertyMapper(entityType, property, propertyMapper);

        return this;
    }

    public FormConfigurator propertyMapper(String property, Class<? extends PropertyMapper> propertyMapper) {
        CrudConfiguration.instance().registerPropertyMapper(entityType, property, propertyMapper);
        return this;
    }

    public FormConfigurator relatedField(String property, String description, String tooltip, Repository repository) {
        return relatedField(property, description, tooltip, repository, null);
    }

    /**
     * Adds a new related property to the form with label
     * "label.identifier.property" where identifier is the form identifier
     * specified in {@link #configure(java.lang.Class, java.lang.String)}.
     *
     * @param property the property name
     * @param repository the repository of the related entities
     * @param renderer the property renderer
     * @return
     */
    public FormConfigurator relatedField(String property, Repository repository, Class<? extends FormFieldRenderer> renderer) {
        return relatedField(property, null, null, repository, renderer);
    }

    public FormConfigurator relatedField(String property, String description, String tooltip, Repository repository, Class<? extends FormFieldRenderer> renderer) {
        if (description == null)
            description = CrudConfiguration.getDefaultDescription(entityType, property);
        if (tooltip == null)
            tooltip = "";
        CrudConfiguration.instance().registerRelatedFormField(entityType, property, getDataType(property), description, tooltip, repository);
        if (renderer != null)
            CrudConfiguration.instance().registerFormFieldRenderer(entityType, property, renderer);
        return this;
    }

    public FormConfigurator relatedField(String property, String description, String tooltip) {
        return relatedField(property, description, tooltip, null, null);
    }

    public FormConfigurator relatedField(String property, String description, String tooltip, Class<? extends FormFieldRenderer> renderer) {
        return relatedField(property, description, tooltip, null, renderer);
    }

    public FormConfigurator param(String property, String key, String value) {
        CrudConfiguration.instance().setPropertyParam(entityType, property, key, value);
        return this;
    }

    //
    private Type getDataType(String property) {
        if (entityType != null) {
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
