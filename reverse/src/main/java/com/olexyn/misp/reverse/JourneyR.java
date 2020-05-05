package com.olexyn.misp.reverse;

import com.olexyn.misp.helper.Ride;

import java.io.IOException;

public class JourneyR implements Runnable {

    private Reverse reverse;

    public JourneyR(Reverse reverse) {
        this.reverse = reverse;
    }


    @Override
    public void run() {
        try {
            Ride _ride = sendPostRide();
            _ride = sendGetRequest(_ride);
            sendPostRideRequestData(_ride);
        } catch (Exception ignored) { }
    }


    Ride sendPostRide() throws IOException {

        final Ride ride = new Ride();

        final String result = Tools.send("POST", reverse.FORWARD_URL, ride.json());

        String _req = new Ride(result).getRequest();
        String request = (_req == null) ? "" : _req;
        ride.setRequest(request);

        return ride;
    }


    Ride sendGetRequest(Ride ride) throws IOException {

        final String result = Tools.send("GET", reverse.APP_URL, ride.getRequest());
        ride.setData(result);

        return ride;
    }


    void sendPostRideRequestData(Ride ride) throws IOException {

        Tools.send("POST", reverse.FORWARD_URL, ride.json());
    }
}