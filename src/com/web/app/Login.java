package com.web.app;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(true);
	    if (session.getAttribute("loggedin") != null && ((boolean)session.getAttribute("loggedin")) == true) {
	        response.sendRedirect("/opp-auth-service/SecondPage");
	        return;
	    }
	    if (request.getParameter("support") != null && request.getParameter("support").equals("Documentation")) {
		    String host = "https://test.oppwa.com";
            String uri = "/authentication/v1/authenticate";
		    String clientId = "8a82941850d047940150d2be3fd20911";
		    String redirectUrl = "http://localhost:8080/opp-auth-service/SecondPage";
		    
		    StringBuilder sb = new StringBuilder();
		    sb.append(host);
		    sb.append(uri);
		    sb.append("?client_id=");
		    sb.append(clientId);
		    sb.append("&redirect_uri=");
		    sb.append(redirectUrl);
		    sb.append("&scope=openid&response_type=code&prompt=none");
		    
		    response.sendRedirect(sb.toString());
		    return;
		}
		else{
			ServletContext sc = getServletContext();
		    RequestDispatcher rd = sc.getRequestDispatcher("/Index.jsp");
		    rd.forward(request, response);
		    return;
		}	    	
}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
