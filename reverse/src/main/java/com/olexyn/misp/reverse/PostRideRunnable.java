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
                if (reverse.available.size() < reverse.AVAILABLE_RIDES_OVERHEAD_TRIGGER) {
                    for (int i = 0; i < reverse.AVAILABLE_RIDES_OVERHEAD; i++) {
                        try {reverse.sendPostRide();} catch (IOException ignored) {}
                    }
                }
            }
        }
    }
}
