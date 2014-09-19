package applica.documentation.admin.search;

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
@Form(DescriptionSearchForm.EID)
@FormRenderer(SearchFormRenderer.class)
public class DescriptionSearchForm implements Entity {
    public static final String EID = "descriptionsearchform";

    private Object id;

    @FormField(description = "label.description")
    private String description;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
