package com.olexyn.misp.reverse;

public class ReverseApp implements Runnable {


    public static void main(String... args) {
        new ReverseApp().doRun();
    }


    @Override
    public void run() {

        doRun();

    }


    private void doRun() {
        Reverse reverse = new Reverse();

        reverse.FORWARD_URL = "http://localhost:8090/forward";
        reverse.APP_URL = "http://localhost:8090/app";

        reverse.AVAILABLE_RIDES_OVERHEAD_TRIGGER = 1;
        reverse.AVAILABLE_RIDES_OVERHEAD = 2;

        reverse.start();
    }
}
