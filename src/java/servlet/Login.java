/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import beans.Usuario;
import dao.UsuarioDAO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import model.Utiles;

/**
 * Procesamiento de autenticación
 * @author María Galbis
 */
public class Login extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    
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
        ServletContext cont = getServletConfig().getServletContext();
        RequestDispatcher reqDispatcher = cont.getRequestDispatcher("/Inicio");
        reqDispatcher.forward(request, response);
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
        Long timeIni = System.currentTimeMillis();
        
        try {
            //recogemos los parámetros
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String accion = request.getParameter("accion");
            
            HttpSession Session = request.getSession();

            //obtenemos el parámetro con la operacion a realizar (abrir sesion/cerrar sesion)
            switch (accion) {
                case "abrir":

                    if (Utiles.userExists(login)) {
                        int id = Utiles.passValidate(login, password);
                        
                        if (id > 0) {
                            Usuario usuario = new Usuario(id);
                            usuario.setDocumentoListPages(usuarioDAO.listarDocumentos(id, 10, 10));
                            usuario.setDocumentoList(usuarioDAO.listarDocumentos(id));
                            Session.setAttribute("usuario", usuario);
                            Session.removeAttribute("error");
                        } else {
                            Session.setAttribute("error", "Password incorrecto");
                        }
                        
                    } else {
                        Session.setAttribute("error", "El usuario introducido no existe");
                    }

                    break;
                case "cerrar":
                    Session.invalidate();
            }

        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Long timeEnd = System.currentTimeMillis();
            System.out.println("Duración de procesado "+getServletName()+": "+(timeEnd - timeIni));
            processRequest(request, response);
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
