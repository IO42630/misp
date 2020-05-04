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

    public String FORWARD_URL = "http://localhost:8090/forward";
    public String APP_URL = "http://localhost:8090/app";

    public static int AVAILABLE_RIDES_OVERHEAD_TRIGGER = 1;
    public static int AVAILABLE_RIDES_OVERHEAD = 2;
    public static int AVAILABLE_RIDES = 0;


    final Map<Long, Ride> available = new HashMap<>();
    final Map<Long, Ride> booked = new HashMap<>();
    final Map<Long, Ride> loaded = new HashMap<>();


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

        synchronized (available) {

            available.put(ride.getID(), ride);

        }

        final String result = send("POST", FORWARD_URL, ride.json());

        synchronized (available) {
            AVAILABLE_RIDES--;
            available.remove(ride.getID());



            String _parsed = new Ride(result).getRequest();
            String request = _parsed==null ? "" : _parsed;
            ride.setRequest(request);
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

        synchronized (loaded) {loaded.remove(ride.getID()); }

        send("GET", FORWARD_URL, ride.json());


    }


    private  String send(String method, String urlString, String body) throws IOException {

        if (method.equals("GET")){
            int br =0;
        }
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


