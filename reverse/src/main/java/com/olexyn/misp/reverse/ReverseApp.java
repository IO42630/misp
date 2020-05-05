package com.olexyn.misp.reverse;

import java.io.IOException;

public class ReverseApp implements Runnable {


    public static void main(String... args) throws IOException {
        new ReverseApp().doRun();
    }


    @Override
    public void run() {

        try {
            doRun();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void doRun() throws IOException {
        Reverse reverse = new Reverse();

        reverse.FORWARD_URL = "http://localhost:8090/forward";
        reverse.APP_URL = "http://localhost:8090/app";



        reverse.start();
    }
}
