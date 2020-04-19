package core;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BridgeServlet extends HttpServlet {

    protected static final String MISP_CLIENT_URL = "http://localhost:9090/mispclient/core";

    public Map<Long, Ride> rideMap = new HashMap<>();
    protected RideMapHelper mapHelper = new RideMapHelper(rideMap);

    // #######
    //
    // #######

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String jsonPayload = IOUtils.toString(request.getReader());

        if (jsonPayload.contains("LINK")) {
            Thread handleGetLinkThread = new Thread(() -> {
                try {
                    handleGetRequest(request, response);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            handleGetLinkThread.start();
        } else {
            Ride ridePayload = new Ride(jsonPayload);
            boolean hasReqeust = ridePayload.getRequest() != null;
            boolean hasData = ridePayload.getData() != null;

            if (hasReqeust && hasData) {
                Thread handleGetRideRequestDataThread = new Thread(() -> {
                    try {
                        handleGetRideRequestData(request, response);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                handleGetRideRequestDataThread.start();
            }
        }
    }


    /**
     * handle GET (Link)
     * remove Ride from AvailableRides
     * add Ride to ReservedRides
     * send OK (Ride) to mispclient
     * send OK (Ride) to public
     */
    protected void handleGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        // # send OK (Ride) to public
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
    protected void handleGetRideRequestData(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        // # send OK (EOL)
    }

    // #######
    //
    // #######

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Thread handlePostRideThread = new Thread(() -> {
            try {handlePostRide(request, response); } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        });
        handlePostRideThread.setName("handlePostRideThread");
        handlePostRideThread.start();
    }


    /**
     * handle POST (Ride)
     * add Ride to AvailableRides
     */
    protected void handlePostRide(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        // # send OK (Ride) to mispclient
    }
}