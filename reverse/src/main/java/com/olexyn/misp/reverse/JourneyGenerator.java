package com.olexyn.misp.reverse;


public class JourneyGenerator implements Runnable {

    public int OVERHEAD = 8;
    public int CHECK_DEPLETION_INTERVAL = 500;
    public int START_NEW_JOURNEY_INTERVAL = 100;

    private Reverse reverse;
    private CheckSuppyR checkSuppyR;

    public JourneyGenerator(Reverse reverse , CheckSuppyR checkSuppyR) {
        this.reverse = reverse;
        this.checkSuppyR = checkSuppyR;
    }



    @Override
    public void run() {

        while (true) {
            try {
                Thread journeyT = new Thread(new JourneyR(reverse));
                journeyT.setName("journeyT");
                journeyT.start();

                while (checkSuppyR.getAvailable() > OVERHEAD) { Thread.sleep(CHECK_DEPLETION_INTERVAL); }

                Thread.sleep(START_NEW_JOURNEY_INTERVAL);

            } catch (Exception ignored) { }
        }
    }
}
