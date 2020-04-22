package com.olexyn.mispl.adapter;

import com.olexyn.misp.client.ConnectionHelper;
import com.olexyn.misp.helper.Ride;

import javax.servlet.ServletException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


public class Adapter {

    protected static final String MISP_BRIDGE_URL = "http://localhost:9090/mispbridge/core";
    protected static final String APP_URL = "http://localhost:9090/mispclient";

    public static final int AVAILABLE_RIDES_OVERHEAD_TRIGGER = 2;
    public static final int AVAILABLE_RIDES_OVERHEAD = 4;


    public final Map<Long, Ride> available = new HashMap<>();
    public final Map<Long, Ride> booked = new HashMap<>();
    public final Map<Long, Ride> loaded = new HashMap<>();


    public Adapter() {

        // Thread : while AvailableRides < 256 , add Ride to AvailableRides , send POST (Ride) [DONE]
        Thread postRideThread = new Thread(new PostRideRunnable(this));
        postRideThread.setName("postRideThread");
        postRideThread.start();
    }



    /**
     * Generated by Loop.
     * Prepare payload for the request.
     * Process the parsed response.
     */
    final void sendPostRide() throws IOException, InterruptedException, ServletException {

        final Ride ride = new Ride();

        synchronized (available) {
            available.put(ride.getID(), ride);
        }

        final Ride parsedRide = doSendPostRide(ride);

        synchronized (available) {
            available.remove(ride.getID());
            ride.setRequest(parsedRide.getRequest());
        }

        synchronized (booked) {
            booked.put(ride.getID(), ride);
        }
        sendGetRequest(ride);
    }


    /**
     * Send POST (Ride).
     * Parse response.
     */
    protected Ride doSendPostRide(Ride ride) throws IOException, InterruptedException, ServletException  {
        // send POST (Ride)
        final HttpURLConnection connection = ConnectionHelper.make("POST", MISP_BRIDGE_URL);

        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(ride.json());
        outputStream.flush();
        outputStream.close();

        Ride rideXa= ConnectionHelper.parseRide(connection);
        rideXa.setRequest("ff");
        return rideXa;
    }


    /**
     * Prepare payload for the request.
     * Process the parsed response.
     */
    final void sendGetRequest(Ride ride) throws IOException, InterruptedException {


        ride.setData(doSendGetRequest(ride.getRequest()));

        synchronized (booked) {
            booked.remove(ride.getID());
        }
        synchronized (loaded) {
            loaded.put(ride.getID(), ride);
        }

        sendGetRideRequestData(ride);
    }


    /**
     * Send GET (Request) to App.
     * Parse response.
     */
    protected String doSendGetRequest(String request) throws IOException, InterruptedException {

        // send GET (Ride)
        final HttpURLConnection connection = ConnectionHelper.make("GET", APP_URL);

        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(request);
        outputStream.flush();
        outputStream.close();

        return ConnectionHelper.parseString(connection);
    }


    /**
     * Prepare payload for the request.
     * Process the parsed response.
     */
    final protected void sendGetRideRequestData(Ride ride) throws IOException, InterruptedException {
        doSendGetRideRequest(ride);
        synchronized (loaded) {
            loaded.remove(ride.getID());
        }
    }


    /**
     * Send GET (Ride)(Request)(Data).
     * Parse response.
     */
    protected void doSendGetRideRequest(Ride ride) throws IOException, InterruptedException {

        HttpURLConnection connection = ConnectionHelper.make("GET", MISP_BRIDGE_URL);

        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(ride.json());
        outputStream.flush();
        outputStream.close();
    }
}


/**
 * While AvailableRides < 256 ,
 * add Ride to AvailableRides ,
 * send POST (Ride).
 */
class PostRideRunnable implements Runnable {

    Adapter adapter;

    public PostRideRunnable(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void run() {
        while (true) {
            if (adapter.available.size() < Adapter.AVAILABLE_RIDES_OVERHEAD_TRIGGER) {
                for (int i = 0; i < Adapter.AVAILABLE_RIDES_OVERHEAD; i++) {
                    try {adapter.sendPostRide();} catch (IOException | InterruptedException | ServletException e) { e.printStackTrace(); }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


