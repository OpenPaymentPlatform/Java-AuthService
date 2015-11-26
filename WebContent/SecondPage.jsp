<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Auth Service | Logged In</title>
</head>
<body>
    <p>Name of the user is: ${name}</p>
    <p>Id of the user is: ${id}</p>
    <p>Attached to: ${attached_to}</p>
    
    <a href="https://oppwa.test.ctpe.info/login" target="blank">BIP</a>
    
    <form action="https://test.oppwa.com/authentication/v1/logout">
        <input type="hidden" name="post_logout_redirect_uri" value="http://localhost:8080/opp-auth-service/Logout"/>
        <input type="submit" value="Logout"/>
    </form>
    
    <script type="text/javascript">
    (function(w, d, o, u, r, a, m)  {
        w[r] = w[r] ||
            function() {
                (w[r].q = w[r].q || []).push(arguments);
        };
        a = d.createElement(o),
        m = d.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = u;
        m.parentNode.insertBefore(a, m);
    })(window, document, 'script', 'https://test.oppwa.com/authentication/v1/sessionTracker/oasm.js', '_OASM');
     
    //put your client id instead of {your_client_id}
    _OASM('client_id', '8a82941850d047940150d2be3fd20911');
     
    //callback for whenever the session status is different than expected
    _OASM('on_change', function(){
        //remove user session locally
        //start a new Login Workflow
        window.location.replace("http://localhost:8080/WebApp/Logout");
    });
    
    _OASM('poll', {state:'logged_in', interval: 10000});
    </script>
</body>
</html>