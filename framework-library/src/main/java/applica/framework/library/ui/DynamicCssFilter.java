package applica.framework.library.ui;

import org.apache.commons.io.FilenameUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DynamicCssFilter implements Filter {

    public final static String STYLES_PATH = "/static/styles";
    public final static String THEMES_PATH = "/WEB-INF/themes";
    public final static String THEMES_PARAM = "theme";
    public final static String DEFAULT_THEME = "blue";

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String extension = FilenameUtils.getExtension(httpRequest.getRequestURI());
        if (!extension.equalsIgnoreCase("styles")) {
            chain.doFilter(request, response);
        } else {
            String cleanPath = httpRequest.getRequestURI().substring(httpRequest.getRequestURI().indexOf("/static/styles"));
            String path = httpRequest.getSession().getServletContext().getRealPath(cleanPath);


            String theme = httpRequest.getParameter("theme");
            if (!StringUtils.hasLength(theme)) {

                theme = (String) httpRequest.getSession().getAttribute("theme");
                if (!StringUtils.hasLength(theme)) {
                    theme = DEFAULT_THEME;
                }
            }
            httpRequest.getSession().setAttribute("theme", theme);

            theme = String.format("%s.properties", theme);
            theme = FilenameUtils.concat(httpRequest.getSession().getServletContext().getRealPath(THEMES_PATH), theme);

            DynamicCssFile dynamicCssFile = readCss(path);
            try {
                Properties themeProperties = readProperties(theme);
                dynamicCssFile.applyProperties(themeProperties);
                response.getWriter().print(dynamicCssFile.getContent());
            } catch (Exception e) {
                e.printStackTrace();
                chain.doFilter(request, response);
            }


        }
    }

    @Cacheable("styles")
    private DynamicCssFile readCss(final String path) {
        return new DynamicCssFile(path);
    }

    @Cacheable("themes")
    private Properties readProperties(final String theme) throws Exception, IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(theme));
        return properties;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
