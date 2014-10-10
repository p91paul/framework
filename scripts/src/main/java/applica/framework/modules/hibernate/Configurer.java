package applica.framework.modules.hibernate;

import applica.framework.data.Entity;
import applica.framework.data.Key;
import applica.framework.library.options.OptionsManager;
import applica.framework.utils.Strings;
import applica.framework.utils.TypeUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 09/10/14
 * Time: 10:25
 */
public class Configurer extends XmlBuilder {

    private List<Class<? extends Entity>> entities = new ArrayList<>();
    private OptionsManager options;

    public List<Class<? extends Entity>> getEntities() {
        return entities;
    }

    public void setEntities(List<Class<? extends Entity>> entities) {
        this.entities = entities;
    }

    public OptionsManager getOptions() {
        return options;
    }

    public void setOptions(OptionsManager options) {
        this.options = options;
    }

    public String configure() {
        xml = new StringBuilder();

        raw("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); endl();
        raw("<!DOCTYPE hibernate-configuration PUBLIC \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\" \"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">"); endl();
        open("hibernate-configuration"); endl();

                open("session-factory"); endl();

                raw("<!-- Database connection settings -->"); endl();
                open("property", attr("name", "connection.driver_class")); rawNoIndent(options.get("applica.framework.data.hibernate.connection.driver_class")); closeNoIndent("property"); endl();
                open("property", attr("name", "connection.url")); rawNoIndent(options.get("applica.framework.data.hibernate.connection.url")); closeNoIndent("property");  endl();
                open("property", attr("name", "connection.username")); rawNoIndent(options.get("applica.framework.data.hibernate.connection.username")); closeNoIndent("property");  endl();
                open("property", attr("name", "connection.password")); rawNoIndent(options.get("applica.framework.data.hibernate.connection.password")); closeNoIndent("property");  endl();
                endl();

                raw("<!-- JDBC connection pool (use the built-in) -->");  endl();
                open("property", attr("name", "connection.pool_size")); rawNoIndent("1"); closeNoIndent("property");  endl();
                endl();

                raw("<!-- SQL dialect -->"); endl();
                open("property", attr("name", "dialect")); rawNoIndent(options.get("applica.framework.data.hibernate.dialect")); closeNoIndent("property");  endl();
                endl();

                raw("<!-- Enable Hibernate's automatic session context management -->"); endl();
                open("property", attr("name", "current_session_context_class")); rawNoIndent("thread"); closeNoIndent("property");  endl();
                endl();

                raw("<!-- Echo all executed SQL to stdout -->"); endl();
                open("property", attr("name", "show_sql")); rawNoIndent("false"); closeNoIndent("property");  endl();
                endl();

                raw("<!-- Drop and re-create the database schema on startup -->"); endl();
                open("property", attr("name", "hbm2ddl.auto")); rawNoIndent("update"); closeNoIndent("property");  endl();
                endl();

                raw("<!-- Mapped classes -->"); endl();
                entities.forEach(e -> { openClose("mapping",attr("resource", classToHbm(e.getName()))); endl(); });

            close("session-factory"); endl();

        close("hibernate-configuration"); endl();
        return xml.toString();
    }

    private String classToHbm(String name) {
        return String.format("%s.hbm.xml", name.replace(".", "/"));
    }

}
