<%@ page import="com.leftjoiners.bancosol.proyectobackend.entity.ColaboradorEntity" %>
<%@ page import="com.leftjoiners.bancosol.proyectobackend.entity.UsuarioEntity" %><%--
  Created by IntelliJ IDEA.
  User: dante
  Date: 20/05/2026
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Coordinador</title>
</head>
<body>
<%
    UsuarioEntity coordinador = (UsuarioEntity) request.getAttribute("coordinador");
%>
<jsp:include page="../shared/navbar.jsp"/>

<main>


</main>

<jsp:include page="../shared/footer.jsp"/>

</body>
</html>
