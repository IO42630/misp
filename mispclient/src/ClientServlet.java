//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


public class ClientServlet extends HttpServlet {

    protected static final String MISP_BRIDGE_URL = "http://localhost:9090/mispbridge/core";
    protected static final String APP_URL = "http://localhost:9090";

    public static final int AVAILABLE_RIDES_OVERHEAD = 1;

    public List<Ride> availableRides = new ArrayList<>();
    public List<Ride> bookedRides = new ArrayList<>();
    public List<Ride> loadedRides = new ArrayList<>();

    public ClientServlet() {

        // Thread : while AvailableRides < 256 , add Ride to AvailableRides , send POST (Ride) [DONE]
        new Thread(new PostRideRunnable(this)).start();


    }


    /**
     * # send POST (Ride)
     * Generated by Loop
     */
    void sendPostRide(Ride oldRide) throws IOException, ServletException, InterruptedException {

        HttpURLConnection connection = ConnectionHelper.make("POST", MISP_BRIDGE_URL);

        // send POST (Ride)
        availableRides.add(oldRide);
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(oldRide.json());
        outputStream.flush();
        outputStream.close();

        // handle OK (Ride)
        // remove Ride from AvailableRides
        // add Ride to ReservedRides
        if (connection.getResponseCode() == 200) {
            Ride parsedRide = ConnectionHelper.parseRide(connection);
            if (parsedRide.equals(oldRide)) {
                availableRides.remove(oldRide);
                bookedRides.add(parsedRide);
            }
        }
    }



    /**
     * # send GET (Request) to App
     */
    void sendGetRequest(Ride oldRide) throws IOException {

        // send FOO
        // TODO make sure as many as possible tyes of requests can be forwarded.

        // handle OK (Data)
        // remove Ride from PendingRequests
        // add Ride to PendingData
        // send GET (Ride)(Data)
        String data = "DATA";
        bookedRides.remove(oldRide);
        Ride newRide = oldRide;
        newRide.setData(data);
        loadedRides.add(newRide);
        sendGetRideRequestData(newRide);

    }


    /**
     * # send GET (Ride)(Request)(Data)
     */
    void sendGetRideRequestData(Ride oldRide) throws IOException {

        HttpURLConnection connection = ConnectionHelper.make("GET", MISP_BRIDGE_URL);

        // send GET (Ride)(Request)(Data)
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(oldRide.json());
        outputStream.flush();
        outputStream.close();

        // handle OK (EOL)
        // remove Ride from PendingData
        // add Ride to EOl
        if (connection.getResponseCode() == 200) {
            Ride shellIdRide = ConnectionHelper.parseRide(connection);
            if (shellIdRide.getRideID() != null) {
                loadedRides.remove(oldRide);
            }
        }
    }


}


/**
 * While AvailableRides < 256 ,
 * add Ride to AvailableRides ,
 * send POST (Ride).
 */
class PostRideRunnable implements Runnable {

    ClientServlet clientServlet;

    public PostRideRunnable(ClientServlet clientServlet) {
        this.clientServlet = clientServlet;
    }

    @Override
    public void run() {
        while (true) {
            if (clientServlet.availableRides.size() < ClientServlet.AVAILABLE_RIDES_OVERHEAD) {
                try {clientServlet.sendPostRide(new Ride());} catch (IOException | ServletException | InterruptedException e) { e.printStackTrace(); }
            }
            try {Thread.sleep(500);} catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}


