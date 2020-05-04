package com.olexyn.misp.reverse;

import com.olexyn.misp.helper.Ride;

import java.io.IOException;

class GetRequestRunnable implements Runnable {

    final private Reverse reverse;

    public GetRequestRunnable(Reverse reverse) {
        this.reverse = reverse;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (reverse.booked) {
                if (reverse.booked.size() > 0) {
                    final Ride ride = reverse.booked.entrySet().iterator().next().getValue();
                    Thread t = new Thread(() -> { try { reverse.sendGetRequest(ride); } catch (IOException ignored) { } });
                    t.start();
                }
            }
        }
    }
}
