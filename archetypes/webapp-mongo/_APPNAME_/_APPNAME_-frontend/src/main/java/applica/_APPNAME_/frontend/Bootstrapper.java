package applica._APPNAME_.frontend;

import applica.framework.CrudConfiguration;
import applica.framework.CrudConstants;
import applica.framework.CrudFactory;
import applica.framework.Grid;
import applica.framework.library.cells.renderers.DefaultCellRenderer;
import applica.framework.library.fields.renderers.DefaultFieldRenderer;
import applica.framework.library.forms.processors.DefaultFormProcessor;
import applica.framework.library.forms.renderers.NoFrameFormRenderer;
import applica.framework.library.grids.renderers.DefaultGridRenderer;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Applica (www.applicadoit.com)
 * User: Bruno Fortunato
 * Date: 3/19/13
 * Time: 6:45 PM
 */
public class Bootstrapper {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CrudFactory crudFactory;

    public void init() {
        logger.info("Applica _APPNAME_ started");

        DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(new String[] { "dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd" });
        ConvertUtils.register(dateConverter, Date.class);
        logger.info("Registered date converter with pattern " + dateConverter.getPatterns());

        CrudConfiguration.instance().setCrudFactory(crudFactory);

        Package pack = Bootstrapper.class.getPackage();
        try {
            CrudConfiguration.instance().scan(pack);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error scanning crud configuration: " + e.getMessage());
        }

        CrudConfiguration.instance().registerGridRenderer(CrudConstants.DEFAULT_ENTITY_TYPE, DefaultGridRenderer.class);
        CrudConfiguration.instance().registerFormRenderer(CrudConstants.DEFAULT_ENTITY_TYPE, NoFrameFormRenderer.class);
        CrudConfiguration.instance().registerFormFieldRenderer(CrudConstants.DEFAULT_ENTITY_TYPE, "", DefaultFieldRenderer.class);
        CrudConfiguration.instance().registerFormProcessor(CrudConstants.DEFAULT_ENTITY_TYPE, DefaultFormProcessor.class);
        CrudConfiguration.instance().registerCellRenderer(CrudConstants.DEFAULT_ENTITY_TYPE, "", DefaultCellRenderer.class);

        CrudConfiguration.instance().setParam(CrudConfiguration.DEFAULT_ENTITY_TYPE, Grid.PARAM_ROWS_PER_PAGE, "50");
    }

}
