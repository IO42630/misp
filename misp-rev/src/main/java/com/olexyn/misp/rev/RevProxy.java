package com.olexyn.misp.rev;


import com.olexyn.misp.helper.Ride;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class RevProxy {

    protected static final String MISP_BRIDGE_URL = "http://localhost:9090/mispbridge/core";
    protected static final String APP_URL = "http://localhost:9090/mirror/core";

    public static final int AVAILABLE_RIDES_OVERHEAD_TRIGGER = 4;
    public static final int AVAILABLE_RIDES_OVERHEAD = 8;


    public final Map<Long, Ride> available = new HashMap<>();
    public final Map<Long, Ride> booked = new HashMap<>();
    public final Map<Long, Ride> loaded = new HashMap<>();


    public RevProxy() {

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

        final String result = send("POST", MISP_BRIDGE_URL, ride.json());

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

        send("GET", MISP_BRIDGE_URL, ride.json());

        synchronized (loaded) {loaded.remove(ride.getID()); }
    }


    private static String send(String method, String urlString, String body) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(body);
        outputStream.flush();
        outputStream.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String out;
        StringBuilder sb = new StringBuilder();
        while ((out = in.readLine()) != null) { sb.append(out); }
        in.close();
        return sb.toString();
    }


}


class PostRideRunnable implements Runnable {

    final private RevProxy adapter;

    public PostRideRunnable(RevProxy adapter) {
        this.adapter = adapter;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (adapter.available) {
                if (adapter.available.size() < RevProxy.AVAILABLE_RIDES_OVERHEAD_TRIGGER) {
                    for (int i = 0; i < RevProxy.AVAILABLE_RIDES_OVERHEAD; i++) {
                        try {adapter.sendPostRide();} catch (IOException ignored) {}
                    }
                }
            }
        }
    }
}


class GetRequestRunnable implements Runnable {

    final private RevProxy adapter;

    public GetRequestRunnable(RevProxy adapter) {
        this.adapter = adapter;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (adapter.booked) {
                if (adapter.booked.size() > 0) {
                    final Ride ride = adapter.booked.entrySet().iterator().next().getValue();
                    try { adapter.sendGetRequest(ride); } catch (IOException ignored) {}
                }
            }
        }
    }
}

class GetRideRequestDataRunnable implements Runnable {

    final private RevProxy adapter;

    public GetRideRequestDataRunnable(RevProxy adapter) {
        this.adapter = adapter;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (adapter.loaded) {
                if (adapter.loaded.size() > 0) {
                    final Ride ride = adapter.loaded.entrySet().iterator().next().getValue();
                    try { adapter.sendGetRideRequestData(ride); } catch (IOException ignored) {}
                }
            }
        }
    }
}