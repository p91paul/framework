package applica._APPNAME_.admin;

import applica._APPNAME_.admin.fields.renderers.RolesFieldRenderer;
import applica._APPNAME_.admin.fields.renderers.UserImageFieldRenderer;
import applica._APPNAME_.admin.mapping.RolePropertyMapper;
import applica._APPNAME_.admin.search.RoleSearchForm;
import applica._APPNAME_.admin.search.UsernameSearchForm;
import applica._APPNAME_.domain.data.RolesRepository;
import applica._APPNAME_.domain.data.UsersRepository;
import applica._APPNAME_.domain.model.Role;
import applica._APPNAME_.domain.model.User;
import applica.framework.CrudConfiguration;
import applica.framework.CrudConstants;
import applica.framework.CrudFactory;
import applica.framework.Grid;
import applica.framework.builders.FormConfigurator;
import applica.framework.builders.GridConfigurator;
import applica.framework.library.cells.renderers.DefaultCellRenderer;
import applica.framework.library.crud.acl.CrudPermission;
import applica.framework.library.crud.acl.CrudSecurityConfigurer;
import applica.framework.library.fields.renderers.DefaultFieldRenderer;
import applica.framework.library.fields.renderers.MailFieldRenderer;
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
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 2/21/13
 * Time: 3:37 PM
 */
public class Bootstrapper {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CrudFactory crudFactory;

    public void init() {
        logger.info("Applica Framework app started");

        DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(new String[] { "dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd" });
        ConvertUtils.register(dateConverter, Date.class);

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

        CrudConfiguration.instance().setParam(CrudConfiguration.DEFAULT_ENTITY_TYPE, Grid.PARAM_ROWS_PER_PAGE, "20");

        registerGrids();
        registerForms();
        configureCrudSecurity();
    }

    private void configureCrudSecurity() {
        CrudSecurityConfigurer.instance().configure("user", CrudPermission.NEW, "hasRole('ADMINISTRATOR')");
    }

    private void registerForms() {
        FormConfigurator.build(User.class, "user")
                .repository(UsersRepository.class)
                .field("mail", "label.mail", MailFieldRenderer.class)
                .field("password", "label.password")
                .field("active", "label.active")
                .field("registrationDate", "label.registration_date")
                .field("image", "label.image", UserImageFieldRenderer.class)
                .field("roles", "label.roles", RolesFieldRenderer.class, RolePropertyMapper.class);


        FormConfigurator.build(Role.class, "role")
                .repository(RolesRepository.class)
                .field("role", "label.name");
    }

    private void registerGrids() {
        GridConfigurator.build(User.class, "user")
                .repository(UsersRepository.class)
                .searchForm(UsernameSearchForm.class)
                .column("mail", "label.mail", true)
                .column("active", "label.active", false);

        GridConfigurator.build(Role.class, "role")
                .repository(RolesRepository.class)
                .searchForm(RoleSearchForm.class)
                .column("role", "label.name", true);

    }
}
