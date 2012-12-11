<%-- 
    Document   : pane
    Created on : 06-nov-2012, 2:07:14
    Author     : María Galbis
--%>

<%@page import="java.util.List"%>
<%@page import="beans.*"%>
<%@page import="model.Utiles"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    HttpSession sesion = request.getSession();
    Usuario usuario = (Usuario) sesion.getAttribute("usuario");
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
       

        <title>Panel</title>
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

            <!--Cuerpo-->
            <div id="cuerpo" class="row">
                <div class="offset1 span9"> 
                    <form action="Documentos" name="documentos" method="POST" class="form-horizontal">
                        <%
                            if (usuario.getDocumentoList() != null && !usuario.getDocumentoList().isEmpty()) {
                        %>
                        <table class="table table-condensed">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Título</th>
                                    <th>Fecha</th>
                                    <th class="center">Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%

                                    for (Documento documento : usuario.getDocumentoList()) {
                                %>
                                <tr>
                                    <td><input type="checkbox" name="idsDoc" value="<%=documento.getId()%>" /></td>
                                    <td><%=documento.getTitulo()%></td>
                                    <td><%=Utiles.dateView(documento.getFecha(), true)%></td> 
                                    <td class="right">
                                        <input type="submit" class="btn" id="<%=documento.getId()%>" name="accion" value="Ver">
                                        <input type="submit" class="btn" id="<%=documento.getId()%>" name="accion" value="Editar">
                                    </td>
                                </tr>
                                <%   }%>

                            </tbody>
                        </table>
                    <%  } else {%>       
                            No ha creado ningún documento
                    <%}%> 
                        <div class="left">
                            <!-- se recoge el id del botón que clickamos, que es el mismo que el del documento -->
                            <input type="hidden" id="idDoc" name="idDoc" value="0" />
                        <%if (usuario.getDocumentoList() != null && !usuario.getDocumentoList().isEmpty()) {%>
                            <p><a data-toggle="modal" href="#confir" class="btn">Eliminar seleccionados</a></p>
                        <%}%>
                            <!-- Confirmación-->
                            <div id="confir" class="modal hide fade">
                                <div class="modal-header">
                                    <a class="close" data-dismiss="modal" >&times;</a>
                                    <h3>Confirmación</h3>
                                </div>
                                <div class="modal-body">
                                    ¿Está seguro que desea eliminar los documentos seleccionados?
                                </div>
                                <div class="modal-footer">
                                    <a href="#" class="btn" data-dismiss="modal" >Cerrar</a>
                                    <input type="submit" name="accion" class="btn btn-primary" value="Eliminar" />
                                </div>
                            </div>
                            
                    

                    <p><a href="Javascript:history.go(-1)">&larr; Volver</a></p>
                </div>
                               
</form>
            </div>
        </div>

        <footer class="footer">
            <p>2012 &COPY; María Galbis</p>
            <p>Powered by <a href="http://twitter.github.com/bootstrap/">Twitter Bootstrap</a></p>
        </footer>   
    </div>
</body>
</html>
