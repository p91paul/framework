package applica._APPNAME_.admin.search;

import applica.framework.annotations.Form;
import applica.framework.annotations.FormField;
import applica.framework.annotations.FormRenderer;
import applica.framework.data.Entity;
import applica.framework.library.forms.renderers.SearchFormRenderer;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 3/4/13
 * Time: 4:18 PM
 */
@Form(UsernameSearchForm.EID)
@FormRenderer(SearchFormRenderer.class)
public class UsernameSearchForm implements Entity {
    public static final String EID = "usernamesearchform";

    private Object id;

    @FormField(description = "label.username")
    private String username;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
