<%-- 
    Document   : config
    Created on : 09-nov-2012, 22:32:54
    Author     : María Galbis
--%>
<%@page import="beans.Usuario"%>
<%@page import="model.Config"%>
<%
    HttpSession sesion = request.getSession();
    Usuario usuario = (Usuario) sesion.getAttribute("usuario");
    String path = (String) sesion.getAttribute("configPath");
    
    int docuPages = Integer.parseInt((String) request.getParameter("docuPag"));
    int comPages = Integer.parseInt((String) request.getParameter("comPag"));
    
    Config.setInitPages(usuario.getId(), docuPages, path, "documents");
    Config.setInitPages(usuario.getId(), comPages, path, "comments");

    response.sendRedirect("Inicio");
%>
