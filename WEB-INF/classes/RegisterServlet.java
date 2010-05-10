import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.*;
import java.sql.*;
//import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class RegisterServlet extends HttpServlet implements Servlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, java.io.IOException {
        response.setContentType("text/html");
        java.io.PrintWriter writer = response.getWriter();

        writer.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
        writer.println("<HTML>");
        writer.println("<HEAD>");
        writer.println("<TITLE>A Sample FORM using POST</TITLE>");
        writer.println("</HEAD>");
        writer.println("<BODY BGCOLOR=\"#FDF5E6\">");
        writer.println("<H1 ALIGN=\"CENTER\">A Sample FORM using POST</H1>");
        writer.println("<FORM ACTION=\"/dk/register\"");
        writer.println("METHOD=\"POST\">");
        writer.println("User name:");
        writer.println("<INPUT TYPE=\"TEXT\" NAME=\"userName\"><BR>");
        writer.println("Password:");
        writer.println("<INPUT TYPE=\"TEXT\" NAME=\"password\"><BR>");
        writer.println("Email:");
        writer.println("<INPUT TYPE=\"TEXT\" NAME=\"email\" VALUE=\"\"><BR>");
        writer.println("<HR>");
        writer.println("<CENTER>");
        writer.println("<INPUT TYPE=\"SUBMIT\" VALUE=\"Submit Registration\">");
        writer.println("</CENTER>");
        writer.println("</FORM>");
        writer.println("</BODY>");
        writer.println("</HTML>");

        //Everything in a servlet is written out, like PHP, to the browser
        //writer.println("<html>");
        //writer.println("<head>");
        //writer.println("<title> Register Servlet </title>");
        //writer.println("</head>");
        //writer.println("<body>");
        //writer.println("Hello, world! I'm a servlet, and this time is: " + new java.util.Date().toString());
        //writer.println("</body>");
        //writer.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, java.io.IOException {
        response.setContentType("text/html");
        java.io.PrintWriter writer = response.getWriter();

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String email    = request.getParameter("email");

        boolean success = false;
        try {
            success = registerUser( userName,
                password,
                email
            );
        //} catch (MySQLIntegrityConstraintViolationException ex) {
        } catch (Exception ex) {
            
        }
        //Everything in a servlet is written out, like PHP, to the browser
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title> Register Servlet </title>");
        writer.println("</head>");
        writer.println("<body>");
        if (success) {
            writer.println("Registration Successful!");
        } else {
            writer.println("Registration Failed!");
        }
        writer.println(
            String.format(
                "Username: %s, Password: %s, Email: %s",
                userName,
                password,
                email
            )
        );
        writer.println("</body>");
        writer.println("</html>");
    }

    /**
     * Returns boolean indicating success
     */
    private boolean registerUser(String userName, String password, String email) 
    //throws MySQLIntegrityConstraintViolationException {
    throws Exception {
        boolean result = false;
        Connection conn = null;

        try {
            String dbUser     = "ist239";
            String dbPassword = "password";
            String url        = "jdbc:mysql://localhost/ist239";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, dbUser, dbPassword);
            System.out.println ("Database connection established");

            //fetch that shit
            String query = 
                "INSERT INTO user"
                + " VALUES (NULL, ?, ?, ?)";
            
            PreparedStatement prepStatement = conn.prepareStatement(
                query
            );

            prepStatement.setString(
                1,
                userName
            );
            prepStatement.setString(
                2,
                password
            );
            prepStatement.setString(
                3,
                email
            );

            int retVal = prepStatement.executeUpdate();
            if (retVal == 0) {
                result = true;
            } else {
                System.err.println(
                    String.format(
                        "INSERT FAILED: Invalid user: %s. password: %s, email: %s",
                        userName,
                        password,
                        email
                    )
                );
            }
        }
        catch (Exception e) {
            System.err.println("Cannot connect to database server");
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
        }
        //return result;
        //this was backwards
        return !result;
    }

    public static void main(String[] sa) {
        try {
            new RegisterServlet().registerUser(
                "bob",
                "test",
                "bob@yahoo.com"
            );
        } catch (Exception ex) {
            System.err.println("Test insert failed!");
        }
    }
}
