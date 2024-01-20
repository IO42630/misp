package com.olexyn.misp.reverse.runnable;

import com.olexyn.misp.helper.Ride;
import com.olexyn.misp.reverse.Tools;

import java.io.IOException;

import static com.olexyn.misp.helper.Constants.EMPTY;
import static com.olexyn.misp.helper.Constants.GET;
import static com.olexyn.misp.helper.Constants.POST;
import static com.olexyn.misp.reverse.Reverse.APP_URL;
import static com.olexyn.misp.reverse.Reverse.FORWARD_URL;

public class JourneyR implements Runnable {

    @Override
    public void run() {
        try {
            var ride = sendPostRide();
            sendGetRequest(ride);
            sendPostRideRequestData(ride);
        } catch (Exception ignored) { /* ignored */ }
    }


    private Ride sendPostRide() throws IOException {

        Ride ride = new Ride();

        String result = Tools.send(POST, FORWARD_URL, ride.json());

        String reqStr = new Ride(result).getRequest();
        reqStr = (reqStr == null) ? EMPTY : reqStr;
        ride.setRequest(reqStr);

        return ride;
    }

    private Ride sendGetRequest(Ride ride) throws IOException {
        String result = Tools.send(GET, APP_URL, ride.getRequest());
        ride.setData(result);
        return ride;
    }

    private void sendPostRideRequestData(Ride ride) throws IOException {
        Tools.send(POST, FORWARD_URL, ride.json());
    }

}