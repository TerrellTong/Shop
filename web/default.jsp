<%--
  Created by IntelliJ IDEA.
  User: ty
  Date: 2019/7/13
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        response.sendRedirect(request.getContextPath()+"/indexServlet");
    %>
</body>
</html>
