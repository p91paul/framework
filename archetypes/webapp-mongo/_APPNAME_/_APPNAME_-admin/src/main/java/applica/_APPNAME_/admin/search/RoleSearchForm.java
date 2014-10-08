package applica._APPNAME_.admin.search;

import applica.framework.annotations.Form;
import applica.framework.annotations.FormField;
import applica.framework.annotations.FormRenderer;
import applica.framework.annotations.SearchCriteria;
import applica.framework.data.Filter;
import applica.framework.data.SEntity;
import applica.framework.library.forms.renderers.SearchFormRenderer;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 3/4/13
 * Time: 4:18 PM
 */
@Form(RoleSearchForm.EID)
@FormRenderer(SearchFormRenderer.class)
public class RoleSearchForm extends SEntity {
    public static final String EID = "rolesearchform";

    @FormField(description = "label.name")
    @SearchCriteria(Filter.LIKE)
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
