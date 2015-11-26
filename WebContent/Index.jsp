<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE htm>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Auth Service | Login</title>
</head>
<body>
    <p>You are not logged in, to login, click the button below</p>
    <form action="https://test.oppwa.com/authentication/v1/authenticate">
    <input type="hidden" name="client_id" value="8a82941850d047940150d2be3fd20911"/>
    <input type="hidden" name="redirect_uri" value="http://localhost:8080/opp-auth-service/SecondPage"/>
    <input type="hidden" name="scope" value="openid"/>
    <input type="hidden" name="response_type" value="code"/>
    <input type="submit" value="Login">
    </form>
</body>
</html>