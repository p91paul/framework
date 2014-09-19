package applica.framework.library.fields.renderers;

import applica.framework.Form;
import applica.framework.FormField;
import org.apache.velocity.VelocityContext;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimePickerRenderer extends BaseFieldRenderer {

    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        return "/templates/fields/time.vm";
    }

    @Override
    protected void setupContext(VelocityContext context) {

        super.setupContext(context);

        context.put("defaultDate", new Date());
    }
}
