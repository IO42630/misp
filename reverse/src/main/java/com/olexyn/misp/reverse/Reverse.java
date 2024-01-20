package com.olexyn.misp.reverse;


import com.olexyn.misp.reverse.runnable.CheckSuppyR;
import com.olexyn.misp.reverse.runnable.JourneyGeneratorR;

public class Reverse implements Runnable {

    public static final String FORWARD_URL = System.getenv("forward.url");
    public static final String APP_URL = System.getenv("app.url");
    public static final int CHECK_SUPPLY_INTERVAL_MS = Integer.parseInt(System.getenv("check.supply.interval.ms"));
    public static final int OVERHEAD = 8;
    public static final int CHECK_DEPLETION_INTERVAL = 500;
    public static final int START_NEW_JOURNEY_INTERVAL = 100;


    public void start() {

        CheckSuppyR checkSuppyR = new CheckSuppyR();

        Thread checkSupplyT = new Thread(checkSuppyR);
        checkSupplyT.setName("checkSupplyT");
        checkSupplyT.start();

        Thread journeyGeneratorT = new Thread(new JourneyGeneratorR(checkSuppyR));
        journeyGeneratorT.setName("journeyGeneratorT");
        journeyGeneratorT.start();
    }


    public static void main(String... args) { new Reverse().start(); }


    @Override
    public void run() { start(); }

}


