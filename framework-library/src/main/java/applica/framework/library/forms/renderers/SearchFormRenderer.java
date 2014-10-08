package applica.framework.library.forms.renderers;

import applica.framework.Form;
import applica.framework.FormField;
import org.apache.velocity.VelocityContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class SearchFormRenderer extends BaseFormRenderer {

    private Form form;

    @Override
    public void render(Writer writer, Form form, Map<String, Object> data) {
        this.form = form;

        super.render(writer, form, data);
    }

    @Override
    protected String createTemplatePath(Form form) {
        String templatePath = "/templates/searchForm.vm";
        return templatePath;
    }

    @Override
    protected void setupContext(VelocityContext context) {
        super.setupContext(context);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        for(FormField field : form.getDescriptor().getFields()) {
            if(StringUtils.hasLength(field.getSearchCriteria())) {
                node.put(field.getProperty(), field.getSearchCriteria());
            }
        }
        context.put("criterias", node.toString());
    }
}
