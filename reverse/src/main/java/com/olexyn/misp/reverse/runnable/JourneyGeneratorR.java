package com.olexyn.misp.reverse.runnable;


import com.olexyn.misp.reverse.Reverse;

public class JourneyGeneratorR implements Runnable {

    public int OVERHEAD = 8;
    public int CHECK_DEPLETION_INTERVAL = 500;
    public int START_NEW_JOURNEY_INTERVAL = 100;

    private final Reverse reverse;
    private final CheckSuppyR checkSuppyR;

    public JourneyGeneratorR(Reverse reverse , CheckSuppyR checkSuppyR) {
        this.reverse = reverse;
        this.checkSuppyR = checkSuppyR;
    }



    @Override
    public void run() {
        int LIMIT = 0;
        while (true) {

            try {
                Thread journeyT = new Thread(new JourneyR(reverse));
                journeyT.setName("journeyT");
                journeyT.start();
                LIMIT++;

                while (checkSuppyR.getAvailable() > OVERHEAD ) { Thread.sleep(CHECK_DEPLETION_INTERVAL); }

                Thread.sleep(START_NEW_JOURNEY_INTERVAL);
                if(LIMIT > 2* OVERHEAD){
                    Thread.sleep(CHECK_DEPLETION_INTERVAL);
                    LIMIT = 0;
                }
                // TODO rework this, so it sends - but not too much, and so it wais - but not too long.

            } catch (Exception ignored) { }
        }
    }
}
