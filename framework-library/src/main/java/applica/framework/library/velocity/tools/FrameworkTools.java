package applica.framework.library.velocity.tools;

import applica.framework.ApplicationContextProvider;
import applica.framework.library.cache.Cache;
import applica.framework.library.i18n.Localization;
import applica.framework.library.options.OptionsManager;
import applica.framework.library.velocity.VelocityBuilderProvider;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 2/22/13
 * Time: 9:58 AM
 */
public class FrameworkTools {

    private Log logger = LogFactory.getLog(getClass());
    private Cache cache;
    private WebApplicationContext webApplicationContext;
    private Localization localization;
    private String filesBase;

    public FrameworkTools() {
        cache = ApplicationContextProvider.provide().getBean(Cache.class);
        webApplicationContext = (WebApplicationContext) ApplicationContextProvider.provide();
        localization = new Localization(webApplicationContext);
    }

    public String getScripts() {
        Object scriptsCache = this.cache.get("scripts");
        if(scriptsCache != null) {
            logger.info("Loaded framework scripts from cache");
            return scriptsCache.toString();
        } else {
            logger.info("Loaded framework scripts from template for first time");

            Template template = VelocityBuilderProvider.provide().engine().getTemplate("/templates/other/framework_scripts.vm");
            VelocityContext context = new VelocityContext();

            context.put("wwwBase", webApplicationContext.getServletContext().getContextPath() + "/");

            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            String scripts = writer.toString();
            this.cache.put("scripts", scripts);

            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return scripts;
        }
    }

    public String getStyles() {
        Object stylesCache = this.cache.get("styles");
        if(stylesCache != null) {
            logger.info("Loaded framework styles from cache");
            return stylesCache.toString();
        } else {
            logger.info("Loaded framework styles from template for first time");

            Template template = VelocityBuilderProvider.provide().engine().getTemplate("/templates/other/framework_styles.vm");
            VelocityContext context = new VelocityContext();

            context.put("wwwBase", webApplicationContext.getServletContext().getContextPath() + "/");

            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            String styles = writer.toString();
            this.cache.put("styles", styles);

            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return styles;
        }
    }

    public String getWwwBase() {
        return webApplicationContext.getServletContext().getContextPath() + "/";
    }

    public Localization getLocalization() {
        return localization;
    }

    public String form(String id) {
        String url = String.format("forms/%s", id);
        String action = String.format("forms/process/%s", id);
        boolean ajax = true;

        return form(url, action, ajax);
    }

    public String form(String url, String action, boolean ajax) {
        Template template = VelocityBuilderProvider.provide().engine().getTemplate("/templates/other/render_form.vm");
        VelocityContext context = new VelocityContext();

        url = webApplicationContext.getServletContext().getContextPath() + "/" + url;
        action = webApplicationContext.getServletContext().getContextPath() + "/" + action;

        context.put("wwwBase", webApplicationContext.getServletContext().getContextPath() + "/");
        context.put("url", url);
        context.put("action", action);
        context.put("ajax", ajax);
        context.put("formId", UUID.randomUUID());

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String render = writer.toString();

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return render;
    }

    public String grid(String url) {
        Template template = VelocityBuilderProvider.provide().engine().getTemplate("/templates/other/render_grid.vm");
        VelocityContext context = new VelocityContext();

        context.put("wwwBase", webApplicationContext.getServletContext().getContextPath() + "/");
        context.put("url", webApplicationContext.getServletContext().getContextPath() + "/" + url);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String render = writer.toString();

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return render;
    }

    public String getFilesBase() {
        if(filesBase == null) {
            OptionsManager options = ApplicationContextProvider.provide().getBean(OptionsManager.class);
            filesBase = options.get("fileserver.base");
            Assert.notNull(filesBase, "specify fileserver.base into options");
        }

        return filesBase;
    }
}
