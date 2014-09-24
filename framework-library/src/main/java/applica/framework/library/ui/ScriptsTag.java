package applica.framework.library.ui;

import applica.framework.ApplicationContextProvider;
import applica.framework.library.options.OptionsManager;
import applica.framework.library.utils.FileWalker;
import applica.framework.library.utils.FileWalkerListener;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptsTag extends TagSupport {

    /**
     *
     */
    private static final long serialVersionUID = -1258277555055993650L;

    public static List<ModularJs> jss;
    private static boolean debug = false;

    @Override
    public int doStartTag() throws JspException {
        jss = null;
        if (jss == null) {
            jss = new ArrayList<>();

            OptionsManager options = ApplicationContextProvider.provide().getBean(OptionsManager.class);
            debug = Boolean.parseBoolean(options.get("debug.js"));

            final String scriptsRelativePath = "/static/scripts";
            final String scriptsRealPath = pageContext.getServletContext().getRealPath(scriptsRelativePath).replace("\\", "/");
            final String contextPath = pageContext.getServletContext().getRealPath("/").replace("\\", "/");
            FileWalker fileWalker = new FileWalker();
            fileWalker.walk(scriptsRealPath, new FileWalkerListener() {
                @Override
                public void onFile(File directory, File file) {
                    if (directory.getAbsolutePath().contains("_s_")) return;

                    if (FilenameUtils.getExtension(file.getAbsolutePath().toLowerCase()).equals("js")) {
                        String realPath = file.getAbsolutePath().replace("\\", "/");

                        ModularJs js;
                        try {
                            js = new ModularJs(realPath);
                            String path = js.getPath().replace(contextPath, "").replace("\\", "/");
                            String requestPath = String.format("%s/%s", pageContext.getServletContext().getContextPath(), path).replace("\\", "/");
                            String canonicalName = path.substring(scriptsRelativePath.length()).replace(".js", "").replace("\\", "/");
                            js.setCanonicalName(canonicalName);
                            js.setRequestPath(requestPath);
                            jss.add(js);
                        } catch (ModuleParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onDirectory(File file) {
                }
            });

            jss = ModularJs.resolveDependencies(jss);
        }
        try {
            final JspWriter writer = pageContext.getOut();

            if (debug) {
                for (ModularJs js : jss) {
                    writer.write(String.format("<script type='text/javascript' src='%s'></script>", js.getRequestPath()));
                    writer.write("\r\n");
                }
            } else {
                writer.write("<script type='text/javascript'>");
                writer.write("\r\n");
                for (ModularJs js : jss) {
                    writer.write(js.getScript());
                    writer.write("\r\n");
                }
                writer.write("</script>");
                writer.write("\r\n");
            }
        } catch (IOException e) {
        }
        return super.doStartTag();
    }


}
