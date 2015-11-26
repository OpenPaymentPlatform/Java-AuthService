package com.web.app;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SecondPage
 */
public class SecondPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SecondPage() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		if (session.getAttribute("loggedin") != null && ((boolean)session.getAttribute("loggedin")) == true) {
		    response.sendRedirect("/opp-auth-service/SecondPage.jsp");
		    return;
		}
		
		String code = request.getParameter("code");
		JSONObject userInfoList = null;
		try {
		    if (code != null) {
    		    JSONObject json = getToken(code);
    		    
    		    if (!json.has("access_token")) {
    		        System.out.println("Error: Access token was not set");
    		    }
    		    else {
        			userInfoList = getUserInformation(json.getString("access_token") );
        			if(userInfoList.getString("id") != null){
        				session.setAttribute("loggedin", true);
        			}
        			session.setAttribute("name", userInfoList.getString("name"));
        			session.setAttribute("id", userInfoList.getString("id"));
        			session.setAttribute("attached_to", userInfoList.getJSONArray("attached_to"));
    		    }
		    }
		} catch (JSONException e) {
			e.printStackTrace();
		    //TODO: logging
		}
		response.sendRedirect("/opp-auth-service/SecondPage.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
    protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected JSONObject getToken(String code) throws IOException, JSONException {
	    System.out.println("=== getToken ===");
		String gatewayUrl = "https://test.oppwa.com/authentication/v1/tokens";
		String request = ""
		+ "code=" + code
		+ "&grant_type=authorization_code&redirect_uri=http://localhost:8080/opp-auth-service/SecondPage&scope=openid";
		
		String response = doHttpRequest(gatewayUrl, request, "POST", "application/x-www-form-urlencoded", null);
		
		JSONObject json = new JSONObject(response.toString());
		return json;
	}

	protected JSONObject getUserInformation(String token) throws IOException,
			JSONException {
	    System.out.println("=== getUserInformation ===");
		String gatewayUrl = "https://test.oppwa.com/authentication/v1/users";
		String response = doHttpRequest(gatewayUrl, null, "GET", "application/x-www-form-urlencoded", token);
		return new JSONObject(response.toString());
	}
	
	public String doHttpRequest(String gatewayURL, String request, String method, String contentType, String accessToken) {
        URL url;
        HttpsURLConnection connection = null;
        int responseCode = 501;
        StringBuilder response = null;
        String headers;
        try
        {
            //Create connection
            url = new URL(gatewayURL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type",
                    contentType);
            if (request != null) {
                connection.setRequestProperty("Content-Length", ""
                        + Integer.toString(request.getBytes().length));
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (accessToken == null) {
                String userCredentials = "8a82941850d047940150d2be3fd20911:G9kmGkjb";
                new Base64();
                String basicAuth = "Basic "
                        + new String(Base64.encode(userCredentials.getBytes()));
                connection.setRequestProperty("Authorization", basicAuth);
            }
            else {
                String basicAuth = "bearer " + accessToken;
                connection.setRequestProperty("Authorization", basicAuth);
            }
//            connection.setDefaultHostnameVerifier(new HostnameVerifier()  
//            {   
//                //Should be removed in production system.
//                public boolean verify(String hostname, SSLSession session)  
//                {  
//                    return true;  
//                }
//            }); 
            //Send request
            if (request != null) {
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(request);
                wr.flush();
                wr.close();
            }


            //Get HTTP Response
            responseCode = connection.getResponseCode();
            InputStream is;
            if (responseCode >= 400)
            {  
                is = connection.getErrorStream();
            }
            else
            {
                is = connection.getInputStream();
            }
            headers = getHeaders(connection);
            
            if (is != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                response = new StringBuilder();
                
                while ((line = rd.readLine()) != null)
                {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                is.close();
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            headers = null;
            responseCode = 404;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            headers = null;
            responseCode = 500;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
        if (request != null) {
            System.out.println("request: " + request);
        }
        else if (accessToken != null) {
            System.out.println("accessToken: " + accessToken);
        }
        System.out.println("Response code: " + responseCode);
        System.out.println("Headers:" + headers);
        System.out.println("Response:" + response.toString());
        return response.toString();
    }
    public static String getHeaders(HttpsURLConnection connection) {
        StringBuilder response = new StringBuilder();
        if (connection.getHeaderFields() != null) {
            Map<String, List<String>> header = connection.getHeaderFields();
            for (String key : header.keySet()) {
                if (key != null) {
                    response.append(key);
                    response.append(" : ");
                    for (String value : header.get(key)) {
                        response.append(value);
                    }
                    response.append("\r");
                }
            }
        }
        return response.toString();
    }
}
