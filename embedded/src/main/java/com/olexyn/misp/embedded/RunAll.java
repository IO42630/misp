package com.olexyn.misp.embedded;

import com.olexyn.misp.reverse.Reverse;

public class RunAll {

    public static void main(String... args) throws InterruptedException {

        Thread serverT = new Thread(new Embedded());
        serverT.start();

        Thread.sleep(2000);

        Reverse reverse = new Reverse();
        reverse.FORWARD_URL = "http://localhost:8090/forward";
        reverse.APP_URL = "http://localhost:8090/app";

        Thread reverseT = new Thread(reverse);
        reverseT.start();
    }
}
