package applica.framework.builders;

import applica.framework.CrudConfiguration;
import applica.framework.CrudConfigurationException;
import applica.framework.data.DeleteOperation;
import applica.framework.data.Entity;
import applica.framework.data.Repository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeleteOperationBuilder {

    private static DeleteOperationBuilder s_instance;

    public static DeleteOperationBuilder instance() {
        if (s_instance == null) s_instance = new DeleteOperationBuilder();
        return s_instance;
    }

    private DeleteOperationBuilder() {
    }

    private Log logger = LogFactory.getLog(getClass());

    public DeleteOperation build(String identifier) throws CrudConfigurationException {
        logger.info(String.format("Building delete operation for identifier: %s", identifier));

        Class<? extends Entity> type = CrudConfiguration.instance().getGridTypeFromIdentifier(identifier);

        Repository repository = CrudConfiguration.instance().getGridRepository(type);
        if (repository == null) throw new CrudConfigurationException("Cannot create repository");

        DeleteOperation operation = new DeleteOperation();
        operation.setRepository(repository);

        return operation;
    }
}
