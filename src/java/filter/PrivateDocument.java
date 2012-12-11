/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import beans.Documento;
import beans.Usuario;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author María Galbis
 */
public class PrivateDocument implements Filter {

    private FilterConfig config = null;

    public PrivateDocument() {
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

        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        HttpSession sesion = ((HttpServletRequest) request).getSession();
        String pathInfo = ((HttpServletRequest) request).getRequestURL().toString();


        //si la llamada se hace desde un jsp...
        if (pathInfo != null && pathInfo.endsWith(".jsp")) {
            if (sesion.getAttribute("documento") == null) {
                RequestDispatcher disp = http.getRequestDispatcher("/notfound.jsp");
                disp.forward(request, response);
            } else {
                if (((Documento) sesion.getAttribute("documento")).getPrivado()
                        && ((Usuario) sesion.getAttribute("usuario") == null
                        || (((Documento) sesion.getAttribute("documento")).getUsuario().getId()
                        != ((Usuario) sesion.getAttribute("usuario")).getId()))) {
                    RequestDispatcher disp = http.getRequestDispatcher("/forbidden.jsp");
                    disp.forward(request, response);
                } else {

                    //antes del procesamiento del servlet
                    chain.doFilter(request, response);
                    //después del procesamiento del servlet

                }
            }
        } else {
            //antes del procesamiento del servlet
            chain.doFilter(request, response);
            //después del procesamiento del servlet
        }
    }

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void destroy() {
    }
}
