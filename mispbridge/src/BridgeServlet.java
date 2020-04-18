
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

public class BridgeServlet extends HttpServlet {

    private static final String MISP_CLIENT_URL  = "http://localhost:9090/mispclient/core";

    public List<Ride> availableRides = new ArrayList<>();
    public List<Ride> reservedRides = new ArrayList<>();
    public List<Ride> deliveredRides = new ArrayList<>();
    private List<Ride> newRequests = new ArrayList<>();
    private List<Ride> forwardedRequests = new ArrayList<>();
    private List<Ride> newData = new ArrayList<>();
    private List<Ride> forwardedData = new ArrayList<>();


    public BridgeServlet() {


    }





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


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html><head><title>MispClient</title></head><body bgcolor=white>");
        writer.println("</body></html>");writer.println("<html><head><title>MispClient</title></head><body bgcolor=white>");
        writer.println("</body></html>");
    }


    /**
     * handle POST (Ride)
     * add Ride to AvailableRides
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String json = "foo"; // TODO
        Ride availableRide = new Ride(json);
        availableRides.add(availableRide);
        // TODO wait rill GET(Link) to return OK(Ride) to mispclient


    }



}
