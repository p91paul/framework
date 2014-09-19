package applica.framework.library.fields.renderers;

import applica.framework.FormField;
import applica.framework.library.i18n.Localization;
import applica.framework.library.options.OptionsManager;
import applica.framework.library.velocity.VelocityFormFieldRenderer;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.Writer;
import java.util.List;

@Component
public abstract class FileFieldRenderer extends VelocityFormFieldRenderer {

    public static final String DEFAULT_ACTION = "upload/file";

    public List<String> getAllowedExtensions() {
        return null;
    }

    public String getAction() { return DEFAULT_ACTION; }
    public String getPath() { return null; }

    @Override
    public void render(Writer writer, FormField field, Object value) {
        Assert.isTrue(DEFAULT_ACTION.equals(getAction()) && StringUtils.hasLength(getPath()), getClass().getName() + ": getPath() cannot return null (do you override the method?)");

        setTemplatePath("templates/fields/file.vm");

        super.render(writer, field, value);
    }

    @Override
    protected void setupContext(VelocityContext context) {
        super.setupContext(context);

        context.put("action", getAction());
        context.put("extensions", getAllowedExtensions());
        context.put("path", getPath());
    }
}