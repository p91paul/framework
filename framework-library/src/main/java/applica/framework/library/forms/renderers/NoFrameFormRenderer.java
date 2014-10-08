package applica.framework.library.forms.renderers;

import applica.framework.Form;
import org.springframework.stereotype.Component;

public class NoFrameFormRenderer extends BaseFormRenderer {

    @Override
    protected String createTemplatePath(Form form) {
        String templatePath = "/templates/formNoFrame.vm";

        return templatePath;
    }

}
