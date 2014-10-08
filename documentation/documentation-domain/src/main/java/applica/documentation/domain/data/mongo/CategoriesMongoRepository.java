package applica.documentation.domain.data.mongo;

import applica.documentation.domain.data.CategoriesRepository;
import applica.documentation.domain.model.Category;
import applica.framework.data.Entity;
import applica.framework.data.Sort;
import applica.framework.data.mongodb.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 18:58
 */
@Repository("repository-category")
public class CategoriesMongoRepository extends MongoRepository implements CategoriesRepository {

    @Override
    protected String getCollectionName() {
        return "categories";
    }

    @Override
    protected Class<? extends Entity> getType() {
        return Category.class;
    }

    @Override
    public Sort getDefaultSort() {
        return new Sort("priority", false);
    }
}
