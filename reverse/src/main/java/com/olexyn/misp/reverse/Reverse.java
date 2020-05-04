package com.olexyn.misp.reverse;


import com.olexyn.misp.helper.Ride;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Reverse {

    public String FORWARD_URL = "http://localhost:8090/bridge";
    public String APP_URL = "http://localhost:8090/app";

    public int AVAILABLE_RIDES_OVERHEAD_TRIGGER = 1;
    public int AVAILABLE_RIDES_OVERHEAD = 2;


    public final Map<Long, Ride> available = new HashMap<>();
    public final Map<Long, Ride> booked = new HashMap<>();
    public final Map<Long, Ride> loaded = new HashMap<>();


    public void start() {
        Thread postRideThread = new Thread(new PostRideRunnable(this));
        postRideThread.setName("postRideThread");
        postRideThread.start();

        Thread getRequestThread = new Thread(new GetRequestRunnable(this));
        getRequestThread.setName("getRequestThread");
        getRequestThread.start();

        Thread getRideRequestDataThread = new Thread(new GetRideRequestDataRunnable(this));
        getRideRequestDataThread.setName("getRideRequestDataThread");
        getRideRequestDataThread.start();
    }


    void sendPostRide() throws IOException {

        final Ride ride = new Ride();

        synchronized (available) { available.put(ride.getID(), ride); }

        final String result = send("POST", FORWARD_URL, ride.json());

        synchronized (available) {
            available.remove(ride.getID());
            ride.setRequest(new Ride(result).getRequest());
        }

        synchronized (booked) { booked.put(ride.getID(), ride); }
    }


    void sendGetRequest(Ride ride) throws IOException {

        synchronized (booked) {booked.remove(ride.getID()); }

        final String result = send("GET", APP_URL, ride.getRequest());
        ride.setData(result);

        synchronized (loaded) {loaded.put(ride.getID(), ride); }
    }


    void sendGetRideRequestData(Ride ride) throws IOException {

        send("GET", FORWARD_URL, ride.json());

        synchronized (loaded) {loaded.remove(ride.getID()); }
    }


    private static String send(String method, String urlString, String body) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        if (body != null) {
            outputStream.writeBytes(body);
        }

        outputStream.flush();
        outputStream.close();

        int i = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String out;
        StringBuilder sb = new StringBuilder();
        while ((out = in.readLine()) != null) { sb.append(out); }
        in.close();
        return sb.toString();
    }



}


