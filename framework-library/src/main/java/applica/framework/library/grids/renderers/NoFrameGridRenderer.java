package applica.framework.library.grids.renderers;

import applica.framework.Grid;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

public class NoFrameGridRenderer extends BaseGridRenderer {

    @Override
    protected String createTemplatePath(Grid grid) {
        ServletContext context = webApplicationContext.getServletContext();
        String templatePath = context.getRealPath("/WEB-INF/templates/gridNoFrame.vm");

        return templatePath;
    }
}
