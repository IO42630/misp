package com.olexyn.misp.reverse;


public class Reverse implements Runnable {

    public String FORWARD_URL = "http://localhost:8090/forward";
    public String APP_URL = "http://localhost:8090/app";


    public void start() {

        CheckSuppyR  checkSuppyR = new CheckSuppyR(this);

        Thread checkSupplyT = new Thread(checkSuppyR);
        checkSupplyT.setName("checkSupplyT");
        checkSupplyT.start();

        Thread journeyGeneratorT = new Thread(new JourneyGenerator(this, checkSuppyR));
        journeyGeneratorT.setName("journeyGeneratorT");
        journeyGeneratorT.start();
    }


    public static void main(String... args) { new Reverse().start(); }


    @Override
    public void run() { start(); }

}


