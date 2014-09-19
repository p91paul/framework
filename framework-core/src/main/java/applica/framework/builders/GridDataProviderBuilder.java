package applica.framework.builders;

import applica.framework.CrudConfiguration;
import applica.framework.CrudConfigurationException;
import applica.framework.data.Entity;
import applica.framework.data.GridDataProvider;
import applica.framework.data.Repository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GridDataProviderBuilder {

    private Log logger = LogFactory.getLog(getClass());

    private GridDataProviderBuilder() {
    }

    ;

    private static GridDataProviderBuilder s_instance = null;

    public static GridDataProviderBuilder instance() {
        if (s_instance == null)
            s_instance = new GridDataProviderBuilder();

        return s_instance;
    }

    public GridDataProvider build(String identifier) throws CrudConfigurationException {
        logger.info("Creating Form data provider " + identifier);

        try {
            Class<? extends Entity> type = CrudConfiguration.instance().getGridTypeFromIdentifier(identifier);

            if (type == null) {
                throw new CrudConfigurationException("Grid not registered for type: " + identifier);
            }

            GridDataProvider formDataProvider = new GridDataProvider();

            Repository repository = CrudConfiguration.instance().getGridRepository(type);
            if (repository == null) throw new CrudConfigurationException("Cannot create repository");

            formDataProvider.setRepository(repository);

            return formDataProvider;
        } catch (CrudConfigurationException ex) {
            logger.error("Configuration error: " + ex.getMessage());
            throw ex;
        }
    }
}
