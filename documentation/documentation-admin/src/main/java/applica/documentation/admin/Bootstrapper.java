package applica.documentation.admin;

import applica.documentation.admin.cell.renderers.ByIdToStringCellRenderer;
import applica.documentation.admin.fields.renderers.*;
import applica.documentation.admin.mapping.RolePropertyMapper;
import applica.documentation.admin.search.RoleSearchForm;
import applica.documentation.admin.search.UsernameSearchForm;
import applica.documentation.domain.data.RolesRepository;
import applica.documentation.domain.data.UsersRepository;
import applica.documentation.domain.model.*;
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
import applica.framework.library.fields.renderers.TextAreaFieldRenderer;
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

        FormConfigurator.build(Version.class, "version")
                .field("name", "label.name")
                .field("code", "label.code");

        FormConfigurator.build(Category.class, "category")
                .field("code", "label.code")
                .field("title", "label.title")
                .field("priority", "label.priority")
                .field("active", "label.active")
                .field("showInMenu", "label.show_in_menu")
                .field("customHeader", "label.custom_header", HtmlFieldRenderer.class)
                .field("parentCategoryId", "label.parent_category", CategoriesOptionalSelectFieldRenderer.class)
                .field("description", "label.description", TextAreaFieldRenderer.class)
                .field("content", "label.content", HtmlFieldRenderer.class)
        ;

        FormConfigurator.build(Article.class, "article")
                .field("code", "label.code")
                .field("title", "label.title")
                .field("priority", "label.priority")
                .field("active", "label.active")
                .field("showInMenu", "label.show_in_menu")
                .field("customHeader", "label.custom_header", HtmlFieldRenderer.class)
                .field("categoryId", "label.category", CategoriesOptionalSelectFieldRenderer.class)
                .field("versionId", "label.version", VersionsSelectFieldRenderer.class)
                .field("description", "label.description", TextAreaFieldRenderer.class)
                .field("content", "label.content", HtmlFieldRenderer.class)
        ;

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

        GridConfigurator.build(Version.class, "version")
                .column("name", "label.name", true)
                .column("code", "label.code", false);

        GridConfigurator.build(Category.class, "category")
                .column("priority", "label.priority", false)
                .column("code", "label.code", true)
                .column("title", "label.title", true)
                .column("active", "label.active", true)
                .column("showInMenu", "label.show_in_menu", true)
                .column("parentCategoryId", "label.parent_category", false, ByIdToStringCellRenderer.class).param("parentCategoryId", "repository", "repository-category")
        ;

        GridConfigurator.build(Article.class, "article")
                .column("priority", "label.priority", false)
                .column("code", "label.code", true)
                .column("title", "label.title", true)
                .column("active", "label.active", true)
                .column("showInMenu", "label.show_in_menu", true)
                .column("categoryId", "label.category", false, ByIdToStringCellRenderer.class).param("categoryId", "repository", "repository-category")
                .column("versionId", "label.version", false, ByIdToStringCellRenderer.class).param("versionId", "repository", "repository-version")
        ;
    }
}
