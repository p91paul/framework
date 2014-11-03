package applica.framework.library.fields.renderers;

import applica.framework.Form;
import applica.framework.FormField;
import applica.framework.library.i18n.Localization;
import applica.framework.library.velocity.VelocityFormFieldRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 03/11/14
 * Time: 16:32
 */
public abstract class TemplateFormFieldRenderer extends VelocityFormFieldRenderer {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected abstract String createTemplatePath(Form form, FormField formField);

    @Override
    public void render(Writer writer, FormField formField, Object value) {
        String templatePath = createTemplatePath(formField.getForm(), formField);

        logger.info("Loading form template: " + templatePath);

        setTemplatePath(templatePath);

        super.render(writer, formField, value);
    }

    @Override
    protected void setupContext(VelocityContext context) {
        super.setupContext(context);

        Localization localization = new Localization(webApplicationContext);
        context.put("localization", localization);
        context.put("wwwBase", webApplicationContext.getServletContext().getContextPath());
    }

}
