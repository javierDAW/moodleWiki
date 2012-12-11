/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import beans.Usuario;
import dao.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import model.*;
import net.ausiasmarch.common.Convert;

/**
 * Procesamientos iniciales
 * @author María Galbis
 */
public class Inicio extends HttpServlet {

    private int count;
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long timeIni = System.currentTimeMillis();

        HttpSession sesion = request.getSession();

        try {
            //usuario actual
            Usuario usuario = new Usuario();
            if (sesion.getAttribute("usuario") != null) {
                usuario = (Usuario) sesion.getAttribute("usuario");
            }

            //configuración de paginación
            String path = getServletContext().getRealPath("WEB-INF/config.xml");
            int docsPagina = Config.getInitPages(usuario.getId(), path, "documents");
            sesion.setAttribute("configPath", path);


            //privacidad de los documentos
            String cond = "(PRIVADO = 0 OR (PRIVADO = 1 AND ID_USUARIO = " + usuario.getId() + "))";

            //filtro de búsqueda
            String buscar = request.getParameter("buscar");
            if (buscar != null) {
                if(Convert.isValidDate(buscar)){
                    buscar = Utiles.dateToMySQLDate(Convert.parseDate(buscar), false);
                }
                
                cond += " AND (TITULO LIKE('%" + buscar + "%') ";
                cond += " OR CONTENIDO LIKE('%" + buscar + "%') ";
                cond += " OR ETIQUETAS LIKE('%" + buscar + "%') ";
                cond += " OR FECHA LIKE ('%"+ buscar +"%') ";

                for (Usuario u : new UsuarioDAO().findAllUsuario()) {
                    if (u.getNombre().toLowerCase().contains(buscar.toLowerCase())
                            || u.getApe1().toLowerCase().contains(buscar.toLowerCase())
                            || u.getApe2().toLowerCase().contains(buscar.toLowerCase())) {
                        cond += " OR ID_USUARIO = " + u.getId();
                    }
                }

                cond += ") ";
            }

            DocumentoDAO documentoDAO = new DocumentoDAO();
            sesion.setAttribute("allDocuments", (ArrayList) documentoDAO.findAllDocumento(docsPagina, cond));
         
            if(count == 1){
            sesion.setAttribute("dataMenu", (ArrayList) documentoDAO.findAllDocumento(cond));
            count++;
            }

        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Long timeEnd = System.currentTimeMillis();
            System.out.println("Duración de procesado " + getServletName() + ": " + (timeEnd - timeIni));
            ServletContext cont = getServletConfig().getServletContext();
            RequestDispatcher reqDispatcher = cont.getRequestDispatcher("/index.jsp");
            reqDispatcher.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String initial = getServletContext().getInitParameter("pagina");;
       
        try {
            count = Integer.parseInt(initial);
        } catch (NumberFormatException e) {
            count = 0;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
