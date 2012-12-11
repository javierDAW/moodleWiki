<%-- 
    Document   : create
    Created on : 06-nov-2012, 2:08:15
    Author     : María Galbis
--%>

<%@page import="java.util.List"%>
<%@page import="beans.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    HttpSession sesion = request.getSession();
    Usuario usuario = (Usuario) sesion.getAttribute("usuario");
    sesion.setAttribute("insertado", false);
%>


<html>
    <head>
        <meta charset="utf-8">
        <link rel="stylesheet" href="lib/css/redactor.css" />	
        <link rel="stylesheet" href="lib/css/bootstrap.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="lib/css/docs.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="lib/css/style.css" type="text/css" media="screen" />

        <link rel="icon" type="image/ico" href="lib/img/principal_icon.png" />

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="lib/js/bootstrap.js"></script>
        

        <%@include file="redactor.jsp"%>

        <title>Publicar</title>

    </head>
    <body>        
        <%@include file="navbar.jsp"%>

        <div id="general" class="container">

            <!--Cabecera-->
            <div id="cabecera" clas="row">
                <div class="span12">
                    <header class="jumbotron subhead" id="overview">
                        <h1>Documentos</h1>
                    </header>
                </div>
            </div>

            <div id="cuerpo" class="row">
                <div id="formulario" class="offset1 span9">
                    <form class="form-horizontal" name="formulario" action="Documentos" method="POST" accept-charset="ISO-8859-1">
                        <%
                            if (request.getAttribute("documento") != null) {
                                Documento documento = (Documento) request.getAttribute("documento");
                        %>
                        <p>
                            <label for="titulo">Título:</label>
                            <input type="text" name="titulo" value="<%=documento.getTitulo()%>" /> 
                            <span id="titerror" class="error">&nbsp;</span>
                        </p>
                        <p>
                        <p>
                            <textarea cols="80" id="textarea" name="contenido" rows="10">
                                <%=documento.getContenido()%>
                            </textarea>
                        </p>
                        <span id="conterror" class="error">&nbsp;</span>
                        </p>
                        <p>
                            <label for="privado">Visibilidad:</label>
                            <input type="radio" name="privado" value="0" <%=documento.getPrivado() ? "" : "checked"%> />Público
                            <input type="radio" name="privado" value="1" <%=documento.getPrivado() ? "checked" : ""%> />Privado
                        </p>
                        <p>
                            <label for="etiquietas">Etiquetas:</label>
                            <input type="text" name="etiquetas" value="<%=documento.getEtiquetas()%>" /> 
                            <span id="eterror" class="error">&nbsp;</span>
                        </p>

                        <input type="hidden" name="idDoc" value="<%=documento.getId()%>" />
                        <input type="hidden" name="accion" value="actualizar" />
                        <%
                        } else {
                        %>
                        <p>
                            <label for="titulo">Título:</label>
                            <input type="text" name="titulo" /> 
                            <span id="titerror" class="error">&nbsp;</span>
                        </p>
                        <p>
                        <p><textarea class="textarea" id="textarea" name="contenido"></textarea></p>
                        <span id="conterror" class="error">&nbsp;</span>
                        </p>
                        <p>
                            <label for="privado">Visibilidad:</label>
                            <input type="radio" name="privado" value="0" checked />Público
                            <input type="radio" name="privado" value="1" />Privado
                        </p>
                        <p>
                            <label for="etiquietas">Etiquetas:</label>
                            <input type="text" name="etiquetas" /> 
                            <span id="eterror" class="error">&nbsp;</span>
                        </p>

                        <input type="hidden" name="accion" value="nuevo" />
                        <% } %>                       
                        <input type="hidden" name="idUser" value="<%=usuario.getId()%>" />
                        <p><input type="button" class="btn" id="publicar" value="Publicar" /></p>
                    </form>
                <div class="row">
                    <a href="Javascript:history.go(-1)">&larr; Atrás</a>
                </div>
                </div>
            </div>

            <footer class="footer">
                <p>2012 &COPY; María Galbis</p>
                <p>Powered by <a href="http://twitter.github.com/bootstrap/">Twitter Bootstrap</a></p>
            </footer> 
        </div>
    </body>
</html>

