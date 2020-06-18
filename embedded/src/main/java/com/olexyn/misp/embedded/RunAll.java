package com.olexyn.misp.embedded;

import com.olexyn.misp.reverse.Reverse;

public class RunAll {

    public static void main(String... args) throws InterruptedException {

        Thread embeddedT = new Thread(new EmbeddedR());
        embeddedT.start();

        Thread.sleep(2000);

        Reverse reverse = new Reverse();
        reverse.FORWARD_URL = "http://localhost:8090/forward";
        reverse.APP_URL = "http://localhost:8090/app";
        //reverse.APP_URL = "https://olexyn.com/wp/";


        Thread reverseT = new Thread(reverse);
        reverseT.start();
    }
}
