package applica.documentation.domain.facade;

import applica.documentation.domain.data.ArticlesRepository;
import applica.documentation.domain.data.CategoriesRepository;
import applica.documentation.domain.model.Article;
import applica.documentation.domain.model.Category;
import applica.documentation.domain.model.Filters;
import applica.framework.builders.LoadRequestBuilder;
import applica.framework.data.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 16/09/14
 * Time: 10:26
 */
@Component
public class CategoriesFacade {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ArticlesRepository articlesRepository;

    private void doLoadCategory(Category parent) {
        List<Category> children = categoriesRepository
                .find(LoadRequestBuilder.build()
                        .eq(Filters.CATEGORY_PARENT_CATEGORY_ID, parent.getId())
                        .eq(Filters.CMSOBJECT_ACTIVE, true)
                ).getRows(Category.class);

        List<Article> articles = articlesRepository
                .find(LoadRequestBuilder.build()
                        .eq(Filters.ARTICLE_CATEGORY_ID, parent.getId())
                        .eq(Filters.CMSOBJECT_ACTIVE, true)
                ).getRows(Article.class);


        parent.setChildren(children);
        parent.setArticles(articles);

        children.forEach((c) -> doLoadCategory(c));
    }

    public Category loadCategoryTree(String code) {
        Category root = categoriesRepository
                .find(LoadRequestBuilder.build().eq(Filters.CMSOBJECT_CODE, code).eq(Filters.CMSOBJECT_ACTIVE, true))
                .getOne(Category.class);

        if (root != null) {
            doLoadCategory(root);
        }

        return root;
    }

    public Category loadCategoryTree(Key id) {
        if (id == null || !StringUtils.hasLength(id.getStringValue())) {
            return null;
        }
        Category root = ((Category) categoriesRepository.get(id.getValue()));
        if (root != null) {
            doLoadCategory(root);
        }

        return root;
    }

}
