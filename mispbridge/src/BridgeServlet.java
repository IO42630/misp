
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class BridgeServlet extends HttpServlet {

    private static final String MISP_CLIENT_URL  = "http://localhost:9090/mispclient/core";

    private List<String> list = new ArrayList<>();


    public BridgeServlet() {


    }



    // handle POST (Ride)
    // add Ride to AvailableRides

    // handle GET (Link)
    // remove Ride from AvailableRides
    // add Ride to ReservedRides
    // send OK (Ride) to mispclient
    // send OK (Ride) to public

    // handle GET (Ride)
    // if Ride in ReservedRides,
    // remove Ride from ReservedRides
    // add Ride to Deliveredrides

    // handle GET (Ride)(Request)
    // if Ride in DeliveredRides
    // remove Ride from DeliveredRides
    // add Ride to NewRequest
    // send OK (Ride)(Request)
    // remove Ride from NewRequest
    // add Ride to ForwardedRequest

    // handle GET (Ride)(Data)
    // if Ride in ForwardedRequest
    // remove Ride from ForwardedRequest
    // add Ride to NewData
    // send OK (Ride)(Data)
    // remove Ride from NewData
    // add Ride to ForwardedData
    // send OK (EOL)
    // remove Ride from ForwardedData
    // add Ride to EOL


    // # send OK (Ride) to mispclient

    // # send OK (Ride) to public

    // # send OK (Ride)(Request)

    // # send OK (Ride)(Data)

    // # send OK (EOL)



    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>MispBridge</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=white>");
        writer.println("<table border=\"0\">");
        writer.println("<tr>");
        writer.println("<td>");
        writer.println("<img src=\"images/tomcat.gif\">");
        writer.println("</td>");
        writer.println("<td>");
        writer.println("<h1>Sample Application Servlet</h1>");
        writer.println("This is the output of a servlet that is part of");
        writer.println("the BridgeServlet, Very World application.");
        writer.println("</td>");
        writer.println("</tr>");

        writer.println("REQUEST");
        writer.println(request.getRequestURI());
        writer.println(request.getRequestURL());
        writer.println(request.getMethod());

        for(String s :  list){
            writer.println(s);
        }





        writer.println("</table>");
        writer.println("</body>");
        writer.println("</html>");
    }



    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        list.add(request.getRemoteUser());
    }



}
