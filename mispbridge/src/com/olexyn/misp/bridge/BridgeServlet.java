package com.olexyn.misp.bridge;

import com.olexyn.misp.helper.Ride;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class BridgeServlet extends HttpServlet {

    protected static final String MISP_CLIENT_URL = "http://localhost:9090/mispclient/core";

    public final Map<Long, Ride> available = new HashMap<>();
    public final Map<Long, Ride> booked = new HashMap<>();
    public final Map<Long, Ride> loaded = new HashMap<>();

    // #######
    //
    // #######

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {




            String payload = IOUtils.toString(request.getReader());

            JSONObject obj = new JSONObject();
            try {
                obj = new JSONObject(payload);
            }catch (JSONException jsonE){
                int br = 0;
                Thread handleGetUserRequestThread = new Thread(() -> {
                    try {
                        handleGetRequest(request, response);
                    } catch (IOException | InterruptedException e) {e.printStackTrace(); }
                });
                handleGetUserRequestThread.setName("handleGetUserRequestThread");
                handleGetUserRequestThread.start();
            }

        Ride ridePayload = new Ride(payload);
        boolean hasID = ridePayload.getID() != null;
        boolean hasRequest = ridePayload.getRequest() != null;
        boolean hasData = ridePayload.getData() != null;




        if (hasID && hasRequest && hasData) {
            Thread handleGetRideRequestDataThread = new Thread(() -> {
                try {
                    handleGetRideRequestData(request, response);
                } catch (IOException | InterruptedException e) { e.printStackTrace(); }
            });
            handleGetRideRequestDataThread.setName("handleGetRideRequestDataThread");
            handleGetRideRequestDataThread.start();
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
        final Ride ride;
        String parsedRequest = IOUtils.toString(request.getReader());


        synchronized (available) {

            while (available.size() < 1) {
                available.notify();
                available.wait();
            }
            // ride exists only locally, thus safe
            ride = available.entrySet().iterator().next().getValue();
            // ride exists only in "available", access through which is sync, thus safe
            available.remove(ride.getID());
            // needed because POST (Ride) wait()s
            available.notify();
        }

        synchronized (booked) {
            // ride exists only locally, thus safe
            booked.put(ride.getID(), ride);
            // ride exists only in "booked", access through which is sync, thus safe
            ride.setRequest(parsedRequest);
            // POST (Ride) wait()s
            booked.notify();
        }

        synchronized (loaded) {

            while (!loaded.containsKey(ride.getID())) {
                loaded.notify();
                loaded.wait();
            }


            // CARE this is tricky
            // what if ride exists in another map, e.g. "available'
            // in that case illegal access is possible
            // be carefull to removing ride from all other references, when adding it to "loaded".
            ride.setData(loaded.remove(ride.getID()).getData());
        }

        response.setStatus(200);
        final PrintWriter writer = response.getWriter();
        writer.write(ride.getData());
        writer.flush();
        writer.close();    }


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
        final String jsonPayload = IOUtils.toString(request.getReader());
        final Ride ride = new Ride(jsonPayload);

        synchronized (booked) {
            booked.remove(ride.getID());
        }

        synchronized (loaded) {
            loaded.put(ride.getID(), ride);
            loaded.notify();
        }
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
        String jsonPayload = IOUtils.toString(request.getReader());
        final Ride ride = new Ride(jsonPayload);

        synchronized (available) {
            available.put(ride.getID(), ride);
            available.notify();
        }

        // ID is final/threadsafe
        while(!(booked.containsKey(ride.getID()))){

        }

        synchronized (booked) {
            //booked.notify();
            //booked.wait();
            ride.setRequest(booked.get(ride.getID()).getRequest());
        }

        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        writer.write(ride.json());
        writer.flush();
        writer.close();
    }
}