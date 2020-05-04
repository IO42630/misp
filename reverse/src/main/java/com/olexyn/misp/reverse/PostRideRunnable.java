package com.olexyn.misp.reverse;

import java.io.IOException;

class PostRideRunnable implements Runnable {

    final private Reverse reverse;

    public PostRideRunnable(Reverse reverse) {
        this.reverse = reverse;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (reverse.available) {
                if (Reverse.AVAILABLE_RIDES < Reverse.AVAILABLE_RIDES_OVERHEAD_TRIGGER) {

                    for (int i = Reverse.AVAILABLE_RIDES; i < Reverse.AVAILABLE_RIDES_OVERHEAD; i++) {
                        Reverse.AVAILABLE_RIDES++;
                        Thread t = new Thread(() -> { try { reverse.sendPostRide(); } catch (IOException ignored) { } });
                        t.start();
                    }
                }
            }
        }
    }
}
