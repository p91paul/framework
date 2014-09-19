package applica.documentation.domain.model;

import applica.framework.data.Key;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 19:06
 */
public class Article extends CmsObject {

    private Key categoryId;
    private Key versionId;
    private String content;

    public Object getCategoryId() {
        return categoryId != null ? categoryId.getValue() : null;
    }

    public void setCategoryId(Object categoryId) {
        this.categoryId = new Key(categoryId);
    }

    public Object getVersionId() {
        return versionId != null ? versionId.getValue() : null;
    }

    public void setVersionId(Object versionId) {
        this.versionId = new Key(versionId);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
