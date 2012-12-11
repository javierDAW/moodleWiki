/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import beans.*;
import dao.*;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import model.Config;

/**
 * Procesamiento de documentos
 *
 * @author María Galbis
 */
public class Documentos extends HttpServlet {

    private DocumentoDAO documentoDAO = new DocumentoDAO();
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String url)
            throws ServletException, IOException {
        ServletContext cont = getServletConfig().getServletContext();
        RequestDispatcher reqDispatcher = cont.getRequestDispatcher(url);
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
        doPost(request, response);
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

        HttpSession Session = request.getSession();   //recogemos la sesión

        //si no consegue realizar ninguna operación el servlet devolverá la 
        //página de error
        String url = request.getRequestURI();

        try {
            //id del usuario. Si es nulo será 0
            int idUser = Session.getAttribute("usuario") != null
                    ? ((Usuario) Session.getAttribute("usuario")).getId() : 0;

            //ud del documento. Si es nulo será 0
            int idDoc = request.getParameter("accion") != null
                    && !request.getParameter("accion").equalsIgnoreCase("eliminar")
                    && request.getParameter("idDoc") != null 
                    &&  Integer.parseInt(request.getParameter("idDoc")) != 0
                    ? Integer.parseInt((String) request.getParameter("idDoc")) : 0;
            
            //paginación de comentarios:
            //recogemos la ruta del archivo xml
            String configPath = (String) Session.getAttribute("configPath");
            
            //recogemos la paginación establecida
            int configComentarios = Config.getInitPages(idUser, configPath, "comments");

            //creamos un usuario a partir del id
            Usuario usuario = new Usuario();
            if (idUser != 0) {
                usuario = new Usuario(idUser);
                usuario.setDocumentoListPages(usuarioDAO.listarDocumentos(idUser, 10, 10));
                usuario.setDocumentoList(usuarioDAO.listarDocumentos(idUser));
            }

            //creamos un documento
            Documento documento = new Documento();

            //obtenemos el parámetro con la operacion a realizar
            if (request.getParameter("accion") != null) {
                switch (request.getParameter("accion").toLowerCase()) {
                    case "nuevo":

                        if (!(boolean) Session.getAttribute("insertado")) {
                            documento.setTitulo(request.getParameter("titulo"));
                            documento.setContenido(request.getParameter("contenido"));
                            documento.setFecha(new Date());
                            documento.setPrivado(request.getParameter("privado").equals("0") ? false : true);
                            documento.setEtiquetas(request.getParameter("etiquetas"));
                            documento.setUsuario(usuario);

                            documentoDAO.insert(documento);
                            Session.removeAttribute("insertado");
                        }

                        Session.setAttribute("usuario", null);
                        usuario = new Usuario(idUser);
                        usuario.setDocumentoListPages(usuarioDAO.listarDocumentos(idUser, 10, 10));
                        usuario.setDocumentoList(usuarioDAO.listarDocumentos(idUser));


                        url = "/panel.jsp";

                        break;
                    case "editar": //acceder a la página de edicion
                        request.setAttribute("documento", new Documento(idDoc));
                        url = "/create.jsp";

                        break;
                    case "actualizar":

                        documento.setId(idDoc);
                        documento.setTitulo(request.getParameter("titulo"));
                        documento.setContenido(request.getParameter("contenido"));
                        documento.setFecha(new Date());
                        documento.setPrivado(request.getParameter("privado").equals("1") ? true : false);
                        documento.setEtiquetas(request.getParameter("etiquetas"));
                        documento.setUsuario(usuario);

                        documentoDAO.update(documento);


                        Session.setAttribute("usuario", null);
                        request.setAttribute("documento", null);
                        usuario = new Usuario(idUser);
                        usuario.setDocumentoListPages(usuarioDAO.listarDocumentos(idUser, 10, 10));
                        usuario.setDocumentoList(usuarioDAO.listarDocumentos(idUser));

                        url = "/panel.jsp";

                        break;
                    case "eliminar":
                        for (Object o : request.getParameterValues("idsDoc")) {
                            documentoDAO.delete(Integer.parseInt((String) o));
                        }


                        Session.setAttribute("usuario", null);
                        usuario = new Usuario(idUser);
                        usuario.setDocumentoListPages(usuarioDAO.listarDocumentos(idUser, 10, 10));
                        usuario.setDocumentoList(usuarioDAO.listarDocumentos(idUser));

                        url = "/panel.jsp";

                        break;
                    case "ver":

                        if (documentoDAO.findDocumentoMap(idDoc) != null
                                && !documentoDAO.findDocumentoMap(idDoc).isEmpty()) {
                            DocumentoDAO docDAO = new DocumentoDAO();
                            Documento doc = new Documento(idDoc);
                            doc.setComentarioListPages(docDAO.listarComentarios(idDoc, configComentarios));
                            request.setAttribute("documento", doc);
                        }

                        url = "/view.jsp";
                }
            }
            if (idUser != 0) {
                Session.setAttribute("usuario", usuario);
            }

        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Long timeEnd = System.currentTimeMillis();
            System.out.println("Duración de procesado " + getServletName() + ": " + (timeEnd - timeIni));
            processRequest(request, response, url);
        }
    }

    @Override
    public void destroy() {
        System.out.println("I'M Melting..........................");
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
