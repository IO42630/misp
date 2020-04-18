import javax.servlet.ServletException;
import java.io.IOException;

public class DoClientThings {


    public void start(ClientServlet clientServlet) {
        // Thread 1
        // while AvailableRides < 256 , add Ride to AvailableRides , send POST (Ride) [DONE]
        Runnable postRideRunnable = new PostRideRunnable(clientServlet);
        Thread postRideThread = new Thread(postRideRunnable);
        postRideThread.start();

        // Thread 2
        // for Rides in ReservedRides, send GET (Ride) [DONE]
        Runnable getRideRunnable = new GetRideRunnable(clientServlet);
        Thread getRideThread = new Thread(getRideRunnable);
        getRideThread.start();
    }


    /**
     * While AvailableRides < 256 ,
     * add Ride to AvailableRides ,
     * send POST (Ride).
     */
    private class PostRideRunnable implements Runnable {

        ClientServlet clientServlet;

        public PostRideRunnable(ClientServlet clientServlet) {
            this.clientServlet = clientServlet;
        }

        @Override
        public void run() {
            while (true) {
                if (clientServlet.availableRides.size() < 256) {
                    try {clientServlet.sendPostRide(new Ride());} catch (IOException | ServletException e) { e.printStackTrace(); }
                }
                try {Thread.sleep(500);} catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }


    /**
     * For Rides in ReservedRides,
     * send GET (Ride).
     */
    private class GetRideRunnable implements Runnable {
        ClientServlet clientServlet;

        public GetRideRunnable(ClientServlet clientServlet) {
            this.clientServlet = clientServlet;
        }

        @Override
        public void run() {
            while (true) {
                for (Ride reservedRide : clientServlet.reservedRides) {
                    try {clientServlet.sendGetRide(reservedRide);} catch (IOException e) { e.printStackTrace(); }
                }
                try {Thread.sleep(500);} catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

}


