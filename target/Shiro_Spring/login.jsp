<%--
  Created by IntelliJ IDEA.
  User: Lembre
  Date: 2018.10.22
  Time: 下午 4:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<h4>Login Page</h4>
<form action="shiro/login" method="POST">
    username: <input type="text" name="username"/>
    <br><br>

    password: <input type="password" name="password"/>
    <br><br>

    <input type="submit" value="Submit"/>
</form>
</body>
</html>
