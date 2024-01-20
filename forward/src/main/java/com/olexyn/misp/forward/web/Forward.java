package com.olexyn.misp.forward.web;

import com.olexyn.misp.helper.JsonHelper;
import com.olexyn.misp.helper.Ride;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.olexyn.misp.helper.Constants.AVAILABLE;

@RestController
public class Forward {

    private static final long WAIT_FOR_USER_REQUEST = 500;

    private final Map<Long, Ride> available = new HashMap<>();
    private final Map<Long, Ride> booked = new HashMap<>();
    private final Map<Long, Ride> loaded = new HashMap<>();


    @GetMapping("/")
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        Thread handleGetRequestThread = new Thread(() -> handleGetRequest(request, response));
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
    private void handleGetRequest(HttpServletRequest request, HttpServletResponse response) {
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
                // ride exists only in AVAILABLE, access through which is sync, thus safe
                available.remove(ride.getId());
                // needed because POST (Ride) wait()s
                available.notify();
            }


            synchronized (booked) {
                // ride exists only locally, thus safe
                booked.put(ride.getId(), ride);
                // ride exists only in "booked", access through which is sync, thus safe
                ride.setRequest(parsedRequest);
                // POST (Ride) wait()s
                booked.notify();
            }


            synchronized (loaded) {

                while (!loaded.containsKey(ride.getId())) {

                    loaded.notify();
                    if (loaded.size() > 0) { break; }
                    loaded.wait();
                }

                // CARE this is tricky
                // what if ride exists in another map, e.g. "available'
                // in that case illegal access is possible
                // be carefull to removing ride from all other references, when adding it to "loaded".
                ride.setData(loaded.remove(ride.getId()).getData());
            }

            response.setStatus(200);
            final PrintWriter writer = response.getWriter();
            writer.write(ride.getData());
            writer.flush();
            writer.close();

        } catch (Exception ignored) {}
    }


    @PostMapping("/")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String payload = IOUtils.toString(request.getReader());

        if (!JsonHelper.isJson(payload)) { return; }

        JSONObject obj = new JSONObject(payload);

        if (obj.has(AVAILABLE)) {
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
            Thread handlePostRideRequestDataT = new Thread(() -> { handlePostRideRequestData(payload); });
            handlePostRideRequestDataT.setName("handlePostRideRequestDataT");
            handlePostRideRequestDataT.start();
            try {handlePostRideRequestDataT.join(); } catch (InterruptedException ignored) { }
        }
    }


    /**
     * Handle POST (Ride)(Request)(Data)
     * Move the Ride from `booked` to `loaded`, so it can be picked up by OK (Data) of GET (Request).
     */
    private void handlePostRideRequestData(String payload) {

        final Ride ride = new Ride(payload);

        synchronized (booked) {
            booked.remove(ride.getId());
        }

        synchronized (loaded) {
            loaded.put(ride.getId(), ride);
            loaded.notify();
        }
    }


    /**
     * Handle POST (Available).
     * Send current # of available Rides to `reverse`.
     */
    private void handlePostAvailable(HttpServletRequest req, HttpServletResponse res) {

        JSONObject obj = new JSONObject().put(AVAILABLE, available.size());

        res.setStatus(200);
        try {
            PrintWriter writer = res.getWriter();
            writer.write(obj.toString());
            writer.flush();
            writer.close();
        } catch (Exception ignored) { /* ignored */ }
    }


    /**
     * Handle POST (Ride).
     * Add Ride to `available`.
     * Wait till a GET (Request) arrives from `user`.
     * Return OK (Ride)(Request) to `reverse`.
     */
    private void handlePostRide(HttpServletRequest request, HttpServletResponse response, String payload) {
        try {

            final Ride ride = new Ride(payload);

            synchronized (available) {
                available.put(ride.getId(), ride);
                available.notify();
            }

            // ID is final/threadsafe
            while (!(booked.containsKey(ride.getId()))) {
                Thread.sleep(WAIT_FOR_USER_REQUEST);
            }

            synchronized (booked) {
                // ride = booked.get(ride.getId());
                ride.setRequest(booked.get(ride.getId()).getRequest());
            }

            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(ride.json());
            writer.flush();
            writer.close();

        } catch (Exception ignored) { /* ignored */ }
    }
}