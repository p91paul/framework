package applica.framework.library.ui;

import applica.framework.library.utils.FileWalker;
import applica.framework.library.utils.FileWalkerListener;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.File;
import java.io.IOException;

public class StylesTag extends TagSupport {

    /**
     *
     */
    private static final long serialVersionUID = -1258277555055993650L;

    @Override
    public int doStartTag() throws JspException {
        final String scriptsPath = pageContext.getServletContext().getRealPath("/static/styles").replace("\\", "/");
        final String contextPath = pageContext.getServletContext().getRealPath("/").replace("\\", "/");
        final JspWriter writer = pageContext.getOut();
        FileWalker fileWalker = new FileWalker();
        fileWalker.walk(scriptsPath, new FileWalkerListener() {
            @Override
            public void onFile(File directory, File file) {
                try {
                    if (directory.getAbsolutePath().contains("_s_")) return;

                    if (FilenameUtils.getExtension(file.getAbsolutePath().toLowerCase()).equals("styles")) {
                        String path = file.getAbsolutePath().replace("\\", "/").replace(contextPath, "");
                        writer.write(String.format("\t<link rel='stylesheet' href='%s/%s' />\r\n", pageContext.getServletContext().getContextPath().replace("\\", "/"), path));
                    }
                } catch (IOException e) {
                }
            }

            @Override
            public void onDirectory(File file) {
            }
        });

        return super.doStartTag();
    }


}
