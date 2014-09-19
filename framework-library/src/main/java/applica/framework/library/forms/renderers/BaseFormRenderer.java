package applica.framework.library.forms.renderers;

import applica.framework.Form;
import applica.framework.library.i18n.Localization;
import applica.framework.library.velocity.VelocityFormRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;
import java.util.Map;

@Component
public class BaseFormRenderer extends VelocityFormRenderer {
    private Log logger = LogFactory.getLog(getClass());

    @Override
    public void render(Writer writer, Form form, Map<String, Object> data) {
        String templatePath = createTemplatePath(form);
        setTemplatePath(templatePath);

        logger.info("Loading form template: " + templatePath);

        super.render(writer, form, data);
    }

    protected String createTemplatePath(Form form) {
        String templatePath = "/templates/form.vm";

        return templatePath;
    }

}
