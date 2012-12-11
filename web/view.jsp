<%-- 
    Document   : view
    Created on : 06-nov-2012, 2:08:03
    Author     : María Galbis
--%>

<%@page import="beans.*"%>
<%@page import="dao.*"%>
<%@page import="model.Utiles"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    HttpSession sesion = request.getSession();
    
    Usuario usuario = new Usuario();
    if (sesion.getAttribute("usuario") != null) {
        usuario = (Usuario) sesion.getAttribute("usuario");
    }
    
    Documento documento = (Documento) request.getAttribute("documento");


%>
<html>
    <head>
        <meta charset="utf-8">
        <link rel="stylesheet" href="lib/css/bootstrap.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="lib/css/docs.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="lib/css/style.css" type="text/css" media="screen" />

        <link rel="icon" type="image/ico" href="lib/img/principal_icon.png" />

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="lib/js/bootstrap.js"></script>
       

        <title>Proyecto-Blog</title>
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

            <div id="cuerpo" class="row">
                <div class="span8">      
                    <%
                        if (request.getAttribute("documento") != null) {
                    %>

                    <div class="row documento">
                        <div class="row">
                            <div class="span2 info">
                                <p>
                                    <span><%=Utiles.dateView(documento.getFecha(), true)%></span>
                                </p>
                                <p>
                                    <span>por</span> <a href="#" title=""><%=documento.getUsuario().getNombre()%></a>
                                </p>
                                <p>
                                    <%=documento.getEtiquetas()%>
                                </p>
                            </div>
                            <div class="span5 post">
                                <h2 class="titulo"><%=documento.getTitulo()%></h2>
                                <div class="entry">
                                    <%=documento.getContenido()%>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%
                        }
                    %>
                   
                </div>
                    <div id="menu" class="span3">
                    <form class="row form-search" action="Inicio" method="post">
                        <p class="right"><input type="text" name="buscar" class="input-medium search-query"></p>
                        <p class="right"><button type="submit" class="btn">Buscar</button></p>
                    </form>
                    
                    <ul class="row">
                        
                    </ul>

                </div> 
                <div class="row span6">
                    <a href="Javascript:history.go(-1)">&larr; Atrás</a>
                </div> 
            </div>

<footer class="footer">
    <p>2012 &COPY; María Galbis</p>
    <p>Powered by <a href="http://twitter.github.com/bootstrap/">Twitter Bootstrap</a></p>
</footer> 
        </div>
    </body>
</html>
