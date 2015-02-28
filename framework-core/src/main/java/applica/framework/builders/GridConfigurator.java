package applica.framework.builders;

import applica.framework.CrudConfiguration;
import applica.framework.data.Entity;
import applica.framework.data.Repository;
import applica.framework.processors.GridProcessor;
import applica.framework.render.CellRenderer;
import applica.framework.render.GridRenderer;
import applica.framework.utils.PropertyPathUtils;

/**
 * Applica (www.applicadoit.com) User: bimbobruno Date: 3/14/13 Time: 4:45 PM
 */
public class GridConfigurator {

    private Class<? extends Entity> entityType;
    private String identifier;

    private GridConfigurator() {

    }

    /**
     * build() method is deprecated. Use configure instead
     *
     * @param entityType
     * @param identifier
     * @return
     */
    @Deprecated
    public static GridConfigurator build(Class<? extends Entity> entityType, String identifier) {
        GridConfigurator gridConfigurator = new GridConfigurator();
        gridConfigurator.entityType = entityType;
        gridConfigurator.identifier = identifier;

        CrudConfiguration.instance().registerGrid(entityType, identifier);

        return gridConfigurator;
    }

    /**
     * Configures a new grid for {@code entityType} with identifier {@code entityType.getSimpleName()}. See {@link #configure(java.lang.Class, java.lang.String)
     * }
     *
     * @param entityType
     * @return
     */
    public static GridConfigurator configure(Class<? extends Entity> entityType) {
        return configure(entityType, entityType.getSimpleName());
    }

    public static GridConfigurator configure(Class<? extends Entity> entityType, String identifier) {
        GridConfigurator gridConfigurator = new GridConfigurator();
        gridConfigurator.entityType = entityType;
        gridConfigurator.identifier = identifier;

        CrudConfiguration.instance().registerGrid(entityType, identifier);

        return gridConfigurator;
    }

    public GridConfigurator renderer(Class<? extends GridRenderer> renderer) {
        CrudConfiguration.instance().registerGridRenderer(entityType, renderer);
        return this;
    }

    public GridConfigurator repository(Class<? extends Repository> repository) {
        CrudConfiguration.instance().registerGridRepository(entityType, repository);
        return this;
    }

    public GridConfigurator formIdentifier(String formIdentifier) {
        CrudConfiguration.instance().registerGridFormIdentifier(identifier, formIdentifier);
        return this;
    }

    public GridConfigurator processor(Class<? extends GridProcessor> gridProcessor) {
        CrudConfiguration.instance().registerGridProcessor(entityType, gridProcessor);
        return this;
    }

    public GridConfigurator searchForm(Class<? extends Entity> formType) {
        CrudConfiguration.instance().registerSearchable(entityType, formType);
        return this;
    }

    public GridConfigurator sortBy(String property, boolean descending) {
        CrudConfiguration.instance().registerSortBy(entityType, property, descending);
        return this;
    }

    /**
     * Adds a new column for property {@code property} with label "label.grididentifier.property". This column will not
     * be linked (which means clicking on it won't open the corresponding form for modifications)
     *
     * @param property the name of the property to show
     * @return the updated GridConfigurator
     */
    public GridConfigurator column(String property) {
        return column(property, null);
    }

    /**
     * Adds a new column for property {@code property} with label "label.grididentifier.property". This column will not
     * be linked (which means clicking on it won't open the corresponding form for modifications)
     *
     * @param property the name of the property to show
     * @param cellRenderer a renderer for the column
     * @return the updated GridConfigurator
     */
    public GridConfigurator column(String property, Class<? extends CellRenderer> cellRenderer) {
        return column(property, null, false, cellRenderer);
    }

    /**
     * Adds a new column for property {@code property} with label "label.identifier.property" where identifier is the
     * form identifier specified in {@link #configure(java.lang.Class, java.lang.String)}.
     *
     * @param property the name of the property to show
     * @param linked wheter or not this column will be linked (which means clicking on it will or won't open the
     * corresponding form for modifications)
     * @return the updated GridConfigurator
     */
    public GridConfigurator column(String property, boolean linked) {
        return column(property, null, linked);
    }

    public GridConfigurator column(String property, String header, boolean linked) {
        return column(property, header, linked, null);
    }

    public GridConfigurator column(String property, String header, boolean linked,
            Class<? extends CellRenderer> cellRenderer) {
        if (header == null)
            header = CrudConfiguration.getDefaultDescription(identifier, property);
        CrudConfiguration.instance().registerGridColumn(entityType, property, header,
                PropertyPathUtils.getDataTypeFromPath(entityType, property), linked);

        if (cellRenderer != null)
            CrudConfiguration.instance().registerCellRenderer(entityType, property, cellRenderer);
        return this;
    }

    public GridConfigurator param(String property, String key, String value) {
        CrudConfiguration.instance().setPropertyParam(entityType, property, key, value);
        return this;
    }
}
