/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author María Galbis
 */
public class PrivatePage implements Filter {

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig config = null;

    public PrivatePage() {
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig config) {
        this.config = config;
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpSession sesion = ((HttpServletRequest) request).getSession();

        if (sesion.getAttribute("usuario") == null) {
            HttpServletRequest http = (HttpServletRequest) request;
            RequestDispatcher disp = http.getRequestDispatcher("/forbidden.jsp");
            disp.forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
        config = null;
    }
}
