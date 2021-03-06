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

    private static final long WAIT_FOR_USER_REQUEST = 500;

    private final Map<Long, Ride> available = new HashMap<>();
    private final Map<Long, Ride> booked = new HashMap<>();
    private final Map<Long, Ride> loaded = new HashMap<>();


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        Thread handleGetRequestThread = new Thread(() -> { handleGetRequest(request, response); });
        handleGetRequestThread.setName("handleGetRequestThread");
        handleGetRequestThread.start();
        try {handleGetRequestThread.join(); } catch (InterruptedException ignored) { }
    }


    /**
     * Handle GET (Request).
     * Remove next Ride from `available`.
     * Put the Ride to `booked`.
     * Wait for Ride to appear in `loaded`. This happens due to POST (Ride)(Request)(Data) from `reverse`.
     * Finally send OK (Data) to `user`.
     */
    protected void handleGetRequest(HttpServletRequest request, HttpServletResponse response) {
        try {

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

                while (!loaded.containsKey(ride.getID())) {

                    loaded.notify();
                    if (loaded.size() > 0) { break; }
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
            writer.close();

        } catch (Exception ignored) {}
    }


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
            Thread handlePostRideT = new Thread(() -> { handlePostRide(request, response, payload); });
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
     * Handle POST (Ride)(Request)(Data)
     * Move the Ride from `booked` to `loaded`, so it can be picked up by OK (Data) of GET (Request).
     */
    protected void handlePostRideRequestData(HttpServletRequest request, HttpServletResponse response, String payload) {

        final Ride ride = new Ride(payload);

        synchronized (booked) {
            booked.remove(ride.getID());
        }

        synchronized (loaded) {
            loaded.put(ride.getID(), ride);
            loaded.notify();
        }
    }


    /**
     * Handle POST (Available).
     * Send current # of available Rides to `reverse`.
     */
    protected void handlePostAvailable(HttpServletRequest request, HttpServletResponse response) {

        JSONObject obj = new JSONObject().put("available", available.size());

        response.setStatus(200);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(obj.toString());
            writer.flush();
            writer.close();
        } catch (Exception ignored) {}
    }


    /**
     * Handle POST (Ride).
     * Add Ride to `available`.
     * Wait till a GET (Request) arrives from `user`.
     * Return OK (Ride)(Request) to `reverse`.
     */
    protected void handlePostRide(HttpServletRequest request, HttpServletResponse response, String payload) {
        try {

            final Ride ride = new Ride(payload);

            synchronized (available) {
                available.put(ride.getID(), ride);
                available.notify();
            }

            // ID is final/threadsafe
            while (!(booked.containsKey(ride.getID()))) {
                Thread.sleep(WAIT_FOR_USER_REQUEST);
            }

            synchronized (booked) {
                // ride = booked.get(ride.getID());
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