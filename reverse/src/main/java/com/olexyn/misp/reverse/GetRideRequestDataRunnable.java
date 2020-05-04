package com.olexyn.misp.reverse;

import com.olexyn.misp.helper.Ride;

import java.io.IOException;

class GetRideRequestDataRunnable implements Runnable {

    final private Reverse reverse;

    public GetRideRequestDataRunnable(Reverse reverse) {
        this.reverse = reverse;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (reverse.loaded) {
                if (reverse.loaded.size() > 0) {
                    final Ride ride = reverse.loaded.entrySet().iterator().next().getValue();
                    try { reverse.sendGetRideRequestData(ride); } catch (IOException ignored) {}
                }
            }
        }
    }
}
