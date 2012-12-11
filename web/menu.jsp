<%-- 
    Document   : menu
    Created on : 05-dic-2012, 10:24:07
    Author     : maria
--%>

<div id="menu" class="span3">
    <form class="row form-search" action="Inicio" method="post">
        <p class="right"><input type="text" name="buscar" class="input-medium search-query"></p>
        <p class="right"><button type="submit" class="btn">Buscar</button></p>
    </form>
    <div class="accordion" id="accordion1">
    <%
        ArrayList menu = (ArrayList) sesion.getAttribute("dataMenu");
        ArrayList anos = Utiles.getYears(menu);
        if (!anos.isEmpty() && anos != null){
            for(int i=0;i<anos.size();i++){
    %>
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion1" href="#collapseOne">
                    <b><%=anos.get(i)%></b>
                </a>
            </div>

            <div id="collapseOne" class="accordion-body collapse">
                <% 
                TreeMap meses = new TreeMap();
                meses = Utiles.getMonths(menu);
                    Iterator<Object> it = meses.keySet().iterator();
                    while (it.hasNext()) {             
                        String clave = (String) it.next();
                %>
                <div class="accordion-inner">
                    <a href="Inicio?buscar=<%=clave%>">&nbsp;&nbsp; <%=Utiles.mes(meses.get(clave).toString())%></a>
                </div>
                <%}%>

            </div>
        </div>


    </div>

</div> 
    <%}}%>
