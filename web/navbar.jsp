<%-- 
    Document   : navbar
    Created on : 16-nov-2012, 1:53:53
    Author     : María Galbis
--%>
<%@page import="java.util.*"%>
<%@page import="beans.*"%>
<%@page import="model.*"%>
<script src="lib/js/jsapp.js"></script>

<%
    ArrayList docs = (ArrayList) sesion.getAttribute("allDocuments");
%>

<div class="navbar navbar-fixed-top">

    <div class="navbar-inner">

        <div id="control" class="container">

            <span class="brand">Proyecto Moodle</span>

            <div class="nav-collapse">

                <ul class="nav pull-left">
                    <li id="home" class="active"><a href="Inicio"><i class="icon-home icon-white"></i></a></li>
                </ul>
                <div class="navbar-form pull-left offset2 reloj"><span id="reloj"></span></div>
                <ul class="nav pull-right">
                    <li>
                        <a href="Javascript: document.login.submit();">
                            <%=sesion.getAttribute("usuario") == null ? "Entrar" : "Salir"%>
                        </a>
                    </li>
                </ul>

                <form class="navbar-form pull-right" name="login" action="Login" method="post">
                    <%if (sesion.getAttribute("usuario") == null) {%>
                    <input type="text" class="input-small" placeholder="Usuario" name="login">
                    <input type="password" class="input-small" placeholder="Contraseña" name="password">
                    <%}%>
                    <input type="hidden" name="accion" value="<%=sesion.getAttribute("usuario") == null ? "abrir" : "cerrar"%>" />

                    <!-- Botón invisible: para poder hacer submit tecleando enter desde el input -->
                    <button type="submit" class="hide"></button>
                </form>

                <%if (sesion.getAttribute("usuario") != null) {%>
                <ul class="nav pull-right">

                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <i class="icon-user icon-white"></i> <%=usuario.getNombre()%> <b class="caret"></b>
                        </a>

                        <ul class="dropdown-menu">
                            <li><a href="panel.jsp"><i class="icon-folder-open"></i> Mis documentos</a></li>
                            <li><a href="create.jsp"><i class="icon-plus"></i> Crear documento</a></li>
                            <li class="divider"></li>
                            <li><a data-toggle="modal" href="#config"><i class="icon-lock"></i> Configuración</a></li>
                        </ul>
                    </li>

                </ul>
                <%}%>

            </div>

            <%
                if (sesion.getAttribute("error") != null) {
            %>
            <div id="error" class="alert fade in">
                <a class="close" data-dismiss="alert" href="#">&times;</a>
                <strong>&iexcl;Error!</strong> <%=sesion.getAttribute("error")%>.
            </div>
            <%
                }
            %>

        </div>


    </div>

</div>


<!-- Configuration form -->
<%
    if (sesion.getAttribute("configPath") != null) {
        int docuPag = Config.getInitPages(usuario.getId(), (String) sesion.getAttribute("configPath"), "documents");
        int comPag = Config.getInitPages(usuario.getId(), (String) sesion.getAttribute("configPath"), "comments");
%>
<div id="config" class="modal hide fade">
    <div class="modal-header">
        <a class="close" data-dismiss="modal" >&times;</a>
        <h3>Configuración</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" action="config.jsp" method="POST" name="paginacion">
            <fieldset>
                <p><h4 class="span6">Paginación</h4></p>
                <div class="control-group">
                    <label class="control-label" for="docuPag">Documentos</label>
                    <div class="controls">
                        <select class="span1" name="docuPag" id="docuPag">
                            <%for (int i = 1; i <= 10; i++) {%>
                            <option<%=docuPag == i ? " selected=\"selected\"" : ""%>><%=i%></option>
                            <%}%>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="comPag">Comentarios</label>
                    <div class="controls">
                        <select class="span1" name="comPag" id="comPag">
                            <%for (int i = 1; i <= 10; i++) {%>
                            <option<%=comPag == i ? " selected=\"selected\"" : ""%>><%=i%></option>
                            <%}%>
                        </select>
                    </div>
                </div>
            </fieldset>
            <!-- Para volver a la página desde donde hemos usado el formulario -->
            <input type="hidden" name="requestUrl" value="<%=request.getRequestURL().toString()%>" />
        </form>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal" >Cerrar</a>
        <a href="Javascript:document.paginacion.submit();" class="btn btn-primary">Guardar cambios</a>
    </div>
</div>
<%}%>
