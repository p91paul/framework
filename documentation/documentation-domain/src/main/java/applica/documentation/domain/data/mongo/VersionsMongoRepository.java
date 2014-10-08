package applica.documentation.domain.data.mongo;

import applica.documentation.domain.data.VersionsRepository;
import applica.documentation.domain.model.Version;
import applica.framework.data.Entity;
import applica.framework.data.mongodb.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 18:58
 */
@Repository("repository-version")
public class VersionsMongoRepository extends MongoRepository implements VersionsRepository {

    @Override
    protected String getCollectionName() {
        return "versions";
    }

    @Override
    protected Class<? extends Entity> getType() {
        return Version.class;
    }
}
