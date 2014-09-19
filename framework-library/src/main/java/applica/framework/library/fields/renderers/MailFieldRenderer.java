package applica.framework.library.fields.renderers;

import applica.framework.Form;
import applica.framework.FormField;
import org.springframework.stereotype.Component;

@Component
public class MailFieldRenderer extends BaseFieldRenderer {

    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        return "/templates/fields/mail.vm";
    }

}
