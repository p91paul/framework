package applica.documentation.domain.model;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 17/09/14
 * Time: 16:50
 */
public class Content {

    private Object value;
    private String content;

    public Content(Object value, String content) {
        this.value = value;
        this.content = content;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
