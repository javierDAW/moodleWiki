<%-- 
    Document   : forbbiden.jsp
    Created on : 18-nov-2012, 1:33:43
    Author     : mgalbis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="beans.*"%>
<!DOCTYPE html>
<%
    HttpSession sesion = request.getSession();

    //usuario actual
    Usuario usuario = new Usuario();
    if (sesion.getAttribute("usuario") != null) {
        usuario = (Usuario) sesion.getAttribute("usuario");
    }
%>
<html>
    <head>

        <meta charset="utf-8">
        <title>Error 403</title>
        <link rel="stylesheet" href="lib/css/bootstrap.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="lib/css/docs.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="lib/css/style.css" type="text/css" media="screen" />

        <link rel="icon" type="image/ico" href="lib/img/principal_icon.png" />

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="lib/js/bootstrap.js"></script>
      

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
                    <div class="row documento">
                        <h2>No tiene permiso para ver esta página.</h2>

                        <p><a href="Inicio">Volver al inicio</a></p>
                    </div>
                </div>           
                <div id="menu" class="span3">
                    <form class="row form-search" action="Inicio" method="post">
                        <p class="right"><input type="text" name="buscar" class="input-medium search-query"></p>
                        <p class="right"><button type="submit" class="btn">Buscar</button></p>
                    </form>

                    <ul class="row">

                    </ul>

                </div> 

            </div>
    </body>
</html>
