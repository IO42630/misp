package com.olexyn.misp.embedded;

import com.olexyn.misp.reverse.ReverseApp;

public class RunAll {

    public static void main(String... args) throws InterruptedException {


        Thread serverT = new Thread(new Embedded());
        serverT.start();

        Thread.sleep(2000);

        Thread reverseT = new Thread(new ReverseApp());
        reverseT.start();
    }

}
