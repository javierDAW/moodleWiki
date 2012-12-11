<%-- 
    Document   : index
    Created on : 23-oct-2012, 17:59:15
    Author     : María Galbis
--%>

<%@page import="java.util.*"%>
<%@page import="beans.*"%>
<%@page import="model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    HttpSession sesion = request.getSession();

    //usuario actual
    Usuario usuario = new Usuario();
    if (sesion.getAttribute("usuario") != null) {
        usuario = (Usuario) sesion.getAttribute("usuario");
    }

    //página actual
    int pagina;
    if (request.getParameter("pagina") != null) {
        pagina = Integer.parseInt(request.getParameter("pagina"));
    } else {
        pagina = Integer.parseInt(application.getContext("/Proyecto-Blog_1").getInitParameter("pagina"));
    }
%>
<html>
    <head>
        <meta charset="utf-8">
        <link rel="stylesheet" href="lib/css/bootstrap.css" type="text/css" media="all" />
        <link rel="stylesheet" href="lib/css/docs.css" type="text/css" media="all" />

        <link rel="icon" type="image/ico" href="lib/img/principal_icon.png" />

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="lib/js/bootstrap.js"></script>
        

        <link rel="stylesheet" href="lib/css/style.css" type="text/css" media="all" />
        <title>Principal</title>
    </head>
    <body>
        <%@include file="navbar.jsp"%>

        <div id="general" class="container">

            <!--Cabecera-->
            <div id="cabecera" class="row">
                <div class="span12">
                    <header class="jumbotron subhead" id="overview">
                        <h1>Documentos</h1>
                    </header>
                </div>
            </div>

            <!--Cuerpo-->           
            <div id="cuerpo" class="row">
                <div class="span8">          

                    <!--Documentos-->
                    <%
                        //ArrayList docs = (ArrayList) sesion.getAttribute("allDocuments");

                        if (!docs.isEmpty() && docs != null && docs.size() >= pagina) {
                            for (Documento d : (ArrayList<Documento>) docs.get(pagina - 1)) {
                                int comentarios = d.getComentarioList().size();

                                String cont = d.getContenido();

                                //solo mostramos el primer párrafo de cada documento en el índice.
                                if (cont.contains("</p>")) {
                                    cont = d.getContenido().substring(0, cont.indexOf("</p>"));
                                }
                    %> 
                    <div class="row documento">
                        <div class="row">
                            <div class="span2 info">
                                <p><span><%=Utiles.dateView(d.getFecha(), false)%></span></p>
                                <p><span>por</span> <a href="#" title=""><%=d.getUsuario().getNombre()%></a></p>
                                <p><a href="#" title="Comment on <%=d.getTitulo()%>"><%=comentarios%> Comentarios</a></p>
                            </div>
                            <div class="span5 post">
                                <h3><a href="Documentos?accion=ver&idDoc=<%=d.getId()%>" ><%=d.getTitulo()%></a></h3>
                                <div class="entry">
                                    <%=cont%>
                                    <p class="right"><a href="Documentos?accion=ver&idDoc=<%=d.getId()%>" >Leer más &rarr;</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%
                        }
                    } else {
                    %>                    
                    <div class="row documento">
                        <div class="row">
                            <span class="error">No se ha encontrado ningún documento</span>
                        </div></div>

                    <%    }
                        int paginas = docs.size() ;
                        if (paginas > 1) {
                    %>
                    
                  <!-- paginacion -->
                    <div class="pagination row">
                        <ul>
                            <li <%=pagina == 1 ? "class=\"disabled\"" : ""%>>
                                <a href="<%=pagina == 1 ? "" : "index.jsp?pagina=" + (pagina - 1)%>">&laquo;</a>
                            </li>
                            <%for (int i = 1; i <= paginas; i++) {%>
                            <li <%=pagina == i ? "class=\"active\"" : ""%>>
                                <a href="index.jsp?pagina=<%=i%>"><%=i%></a>
                            </li>
                            <%}%>
                            <li <%=pagina == paginas ? "class=\"disabled\"" : ""%>>
                                <a href="<%=pagina == paginas ? "" : "index.jsp?pagina=" + (pagina + 1)%>">&raquo;</a>
                            </li>
                        </ul>
                    </div>

                    <%
                        }
                    %>

                </div>      
                    
                    <%@include file="menu.jsp"%>
                

            </div>

            <footer class="footer">
                <p>2012 &COPY; María Galbis</p>
                <p>Powered by <a href="http://twitter.github.com/bootstrap/">Twitter Bootstrap</a></p>
            </footer> 
        </div>


    </body>
</html>