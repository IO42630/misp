package com.olexyn.misp.forward;

import com.olexyn.misp.helper.JsonHelper;
import com.olexyn.misp.helper.Ride;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Forward extends HttpServlet {

    protected static final String MISP_CLIENT_URL = "http://localhost:9090/mispclient/core";

    public final Map<Long, Ride> available = new HashMap<>();
    public final Map<Long, Ride> booked = new HashMap<>();
    public final Map<Long, Ride> loaded = new HashMap<>();

    // #######
    //
    // #######

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        Thread handleGetRequestThread = new Thread(() -> {
            try {
                handleGetRequest(request, response);
            } catch (IOException | InterruptedException e) {e.printStackTrace(); }
        });
        handleGetRequestThread.setName("handleGetRequestThread");
        handleGetRequestThread.start();
        try {handleGetRequestThread.join(); } catch (InterruptedException ignored) { }


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


        final ServletInputStream in = request.getInputStream();
        final String parsedRequest = new String(in.readAllBytes());


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

            boolean realcondition = !loaded.containsKey(ride.getID());
            boolean relaxedcondition = loaded.size() == 0;

            while (loaded.size() == 0) {

                loaded.notify();
                if (loaded.size() > 0) {
                    break;
                }
                loaded.wait();

            }


            // CARE this is tricky
            // what if ride exists in another map, e.g. "available'
            // in that case illegal access is possible
            // be carefull to removing ride from all other references, when adding it to "loaded".
            Ride badbad__ride = loaded.entrySet().iterator().next().getValue();
            ride.setData(loaded.remove(badbad__ride.getID()).getData());
            //ride.setData(loaded.remove(ride.getID()).getData());

        }

        response.setStatus(200);
        final PrintWriter writer = response.getWriter();
        writer.write(ride.getData());
        writer.flush();
        writer.close();
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
    protected void handlePostRideRequestData(HttpServletRequest request, HttpServletResponse response, String payload) {

        final String _payload = payload;
        final Ride ride = new Ride(_payload);

        synchronized (booked) {
            booked.remove(ride.getID());
        }

        synchronized (loaded) {
            loaded.put(ride.getID(), ride);
            loaded.notify();
        }
    }


    protected void handlePostAvailable(HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        obj.put("available", available.size());

        response.setStatus(200);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(obj.toString());
            writer.flush();
            writer.close();
        } catch (Exception ignored) {}
    }


    // #######
    //
    // #######

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String payload = IOUtils.toString(request.getReader());

        if (!JsonHelper.isJson(payload)) { return; }

        JSONObject obj = new JSONObject(payload);

        if (obj.has("available")) {
            Thread handlePostAvailableT = new Thread(() -> { handlePostAvailable(request, response); });
            handlePostAvailableT.setName("handlePostAvailableT");
            handlePostAvailableT.start();
            try {handlePostAvailableT.join(); } catch (InterruptedException ignored) { }
        }


        boolean hasData = obj.has("data") && obj.getString("data") != null;

        if (obj.has("id") && !hasData) {
            Thread handlePostRideT = new Thread(() -> { handlePostRide(request, response); });
            handlePostRideT.setName("handlePostRideT");
            handlePostRideT.start();
            try {handlePostRideT.join(); } catch (InterruptedException ignored) { }
        }

        if (obj.has("id") && hasData) {
            Thread handlePostRideRequestDataT = new Thread(() -> { handlePostRideRequestData(request, response, payload); });
            handlePostRideRequestDataT.setName("handlePostRideRequestDataT");
            handlePostRideRequestDataT.start();
            try {handlePostRideRequestDataT.join(); } catch (InterruptedException ignored) { }
        }
    }

    /**
     * handle POST (Ride)
     * add Ride to AvailableRides
     */
    protected void handlePostRide(HttpServletRequest request, HttpServletResponse response) {

        try {
            String jsonPayload = IOUtils.toString(request.getReader());


            final Ride ride = new Ride(jsonPayload);

            synchronized (available) {
                available.put(ride.getID(), ride);
                available.notify();
            }

            // ID is final/threadsafe
            while (!(booked.containsKey(ride.getID()))) {
                Thread.sleep(500);
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

        } catch (Exception ignored) {}

    }
}