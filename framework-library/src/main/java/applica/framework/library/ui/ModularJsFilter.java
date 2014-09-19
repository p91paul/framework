package applica.framework.library.ui;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Servlet Filter implementation class ModularJsFilter
 */
public class ModularJsFilter implements Filter {

    /**
     * Default constructor.
     */
    public ModularJsFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUri = httpRequest.getRequestURI();

        List<ModularJs> jss = ScriptsTag.jss;
        ModularJs js = findByRequestUri(jss, requestUri);
        if (js != null) {
            response.getOutputStream().print(js.getScript());
        } else {
            chain.doFilter(request, response);
        }

    }

    private ModularJs findByRequestUri(List<ModularJs> jss, String requestUri) {
        if (jss == null) return null;
        for (ModularJs js : jss) {
            if (js.getRequestPath().equals(requestUri)) return js;
        }

        return null;
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
