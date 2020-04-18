import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BridgeServlet extends HttpServlet {

    private static final String MISP_CLIENT_URL = "http://localhost:9090/mispclient/core";

    public List<Ride> availableRides = new ArrayList<>();
    public List<Ride> reservedRides = new ArrayList<>();
    public List<Ride> deliveredRides = new ArrayList<>();
    private List<Ride> newRequests = new ArrayList<>();
    private List<Ride> forwardedRequests = new ArrayList<>();
    private List<Ride> newData = new ArrayList<>();
    private List<Ride> forwardedData = new ArrayList<>();

    // #######
    //
    // #######

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String jsonPayload = IOUtils.toString(request.getReader());

        if (jsonPayload.contains("LINK")) {
            handleGetLink(request, response);
        } else {
            Ride ridePayload = new Ride(jsonPayload);
            boolean hasReqeust = ridePayload.getRequest() != null;
            boolean hasData = ridePayload.getData() != null;

            if (!hasReqeust && !hasData) { handleGetRide(request, response);}
            if (hasReqeust && !hasData) { handleGetRideRequest(request, response);}
            if (hasReqeust && hasData) { handleGetRideRequestData(request, response);}
        }
    }


    /**
     * handle GET (Link)
     * remove Ride from AvailableRides
     * add Ride to ReservedRides
     * send OK (Ride) to mispclient
     * send OK (Ride) to public
     */
    private Ride handleGetLink(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (Ride) to mispclient
        // # send OK (Ride) to public
        return new Ride();
    }


    /**
     * handle GET (Ride)
     * if Ride in ReservedRides,
     * remove Ride from ReservedRides
     * add Ride to Deliveredrides
     */
    private Ride handleGetRide(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (Ride)(Request)
        return new Ride();
    }


    /**
     * handle GET (Ride)(Request)
     * if Ride in DeliveredRides
     * remove Ride from DeliveredRides
     * add Ride to NewRequest
     * send OK (Ride)(Request)
     * remove Ride from NewRequest
     * add Ride to ForwardedRequest
     */
    private Ride handleGetRideRequest(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (Ride)(Request)(Data)
        return new Ride();
    }


    /**
     * handle GET (Ride)(Data)
     * if Ride in ForwardedRequest
     * remove Ride from ForwardedRequest
     * add Ride to NewData
     * send OK (Ride)(Data)
     * remove Ride from NewData
     * add Ride to ForwardedData
     * send OK (EOL)
     * remove Ride from ForwardedData
     * add Ride to EOL
     */
    private Ride handleGetRideRequestData(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (EOL)
        return new Ride();
    }

    // #######
    //
    // #######

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        handlePostRide(request, response);
    }


    /**
     * handle POST (Ride)
     * add Ride to AvailableRides
     */
    private Ride handlePostRide(HttpServletRequest request, HttpServletResponse response) {
        return new Ride();
    }
}