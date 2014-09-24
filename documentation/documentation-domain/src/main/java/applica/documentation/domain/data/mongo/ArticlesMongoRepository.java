package applica.documentation.domain.data.mongo;

import applica.documentation.domain.data.ArticlesRepository;
import applica.documentation.domain.model.Article;
import applica.framework.data.Entity;
import applica.framework.data.LoadRequest;
import applica.framework.data.LoadResponse;
import applica.framework.data.Sort;
import applica.framework.data.mongodb.MongoRepository;
import applica.framework.data.mongodb.Query;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 18:58
 */
@Repository("repository-article")
public class ArticlesMongoRepository extends MongoRepository implements ArticlesRepository {

    @Override
    protected String getCollectionName() {
        return "articles";
    }

    @Override
    protected Class<? extends Entity> getType() {
        return Article.class;
    }

    @Override
    public Sort getDefaultSort() {
        return new Sort("priority", false);
    }

}
