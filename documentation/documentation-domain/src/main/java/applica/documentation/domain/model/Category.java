package applica.documentation.domain.model;

import applica.framework.data.Key;

import java.util.ArrayList;
import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 19:01
 */
public class Category extends CmsObject {

    private Key parentCategoryId = new Key(""); //empty string to indicate that is a root category

    private transient List<Category> children = new ArrayList<>();
    private transient List<Article> articles;

    public Object getParentCategoryId() {
        return parentCategoryId != null ? parentCategoryId.getValue() : null;
    }

    public void setParentCategoryId(Object parentCategoryId) {
        this.parentCategoryId = new Key(parentCategoryId);
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
