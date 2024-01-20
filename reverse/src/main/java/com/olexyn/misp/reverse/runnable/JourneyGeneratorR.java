package com.olexyn.misp.reverse.runnable;


import static com.olexyn.misp.reverse.Reverse.CHECK_DEPLETION_INTERVAL;
import static com.olexyn.misp.reverse.Reverse.OVERHEAD;
import static com.olexyn.misp.reverse.Reverse.START_NEW_JOURNEY_INTERVAL;

public class JourneyGeneratorR implements Runnable {

    private final CheckSuppyR checkSuppyR;

    public JourneyGeneratorR(CheckSuppyR checkSuppyR) {
        this.checkSuppyR = checkSuppyR;
    }

    @Override
    public void run() {
        int LIMIT = 0;
        while (true) {

            try {
                Thread journeyT = new Thread(new JourneyR());
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

            } catch (Exception ignored) { /* ignored */ }
        }
    }
}
