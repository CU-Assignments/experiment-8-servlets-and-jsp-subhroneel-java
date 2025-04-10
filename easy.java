import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    
    // For simplicity, we'll hardcode some valid credentials
    // In a real application, these would be stored in a database
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "password123";
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get the submitted username and password
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Set response content type
        response.setContentType("text/html");
        
        // Get a PrintWriter to write the response
        PrintWriter out = response.getWriter();
        
        // HTML header
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login Result</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }");
        out.println(".container { max-width: 600px; margin: 50px auto; padding: 20px; background: white; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        out.println(".success { color: #4CAF50; }");
        out.println(".error { color: #f44336; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        
        // Check if login is successful
        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            // Successful login
            out.println("<h2 class=\"success\">Login Successful</h2>");
            out.println("<p>Welcome, " + username + "! You have successfully logged in.</p>");
            
            // Get current time
            java.util.Date date = new java.util.Date();
            out.println("<p>Login time: " + date + "</p>");
            
            // Add a logout option
            out.println("<p><a href=\"login.html\">Logout</a></p>");
        } else {
            // Failed login
            out.println("<h2 class=\"error\">Login Failed</h2>");
            out.println("<p>Invalid username or password. Please try again.</p>");
            out.println("<p><a href=\"login.html\">Back to Login</a></p>");
        }
        
        // HTML footer
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}