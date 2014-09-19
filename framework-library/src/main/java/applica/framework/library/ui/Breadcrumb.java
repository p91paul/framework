package applica.framework.library.ui;

public class Breadcrumb {
    private String description;
    private String url;

    public Breadcrumb() {
    }

    public Breadcrumb(String description, String url) {
        super();
        this.description = description;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
