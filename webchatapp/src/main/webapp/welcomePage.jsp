<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html>
<head>
    <link rel="stylesheet" href="css/styleLogin.css"/>
    <title>Are you a lonely programmer?</title>
</head>
<body>
<div class="welcomePageBody">
    <form class="userForm" action="/login" method="post">
        <div>
            <label style="color: white">Username</label>
            <input class="inputLoginPassword" type="text" name="username">
        </div>
        <div>
            <label style="color: white">Password</label>
            <input class="inputLoginPassword" type="password" name="pass">
        </div>
        <c:out value="${requestScope.errorMsg}"></c:out>
        <div>
            <button class="buttonSubmit" type="submit">Submit</button>
        </div>
    </form>
</div>
</body>
</html>
