import exchange.ExchangeMock;

import javax.servlet.ServletException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Wraps a ClientServlet so it can be debugged easily, i.e. without running Tomcat.
 */
public class ClientMock extends ClientServlet  {

    private MockSet mockSet;

    public ClientMock(MockSet mockSet){
        super();
        mockSet.clientMock = this;
        this.mockSet = mockSet;
    }

    @Override
    void sendPostRide(Ride ride) throws IOException, ServletException, InterruptedException {

        availableRides.add(ride);

        // Mock Exchange
        ExchangeMock exchange = new ExchangeMock();

        exchange.request.setMethod("POST");
        exchange.request.setContentType("application/json");
        exchange.request.setContent(ride.json().getBytes());

        synchronized (exchange){
            // Mock POST (Ride)
            mockSet.bridgeMock.doPost(exchange.request,exchange.response);
            exchange.notify();
            exchange.wait();

            // handle OK (Ride)(Request)
            Ride parsedRide = new Ride(exchange.response.getContentAsString());
            ride.setRequest(parsedRide.getRequest());
            int i = availableRides.indexOf(ride);
            bookedRides.add(availableRides.remove(i));
            sendGetRequest(ride);
        }



    }


    /**
     * # send GET (Request) to App
     */
    @Override
    void sendGetRequest(Ride oldRide) throws IOException {

        HttpURLConnection connection = ConnectionHelper.make("GET", APP_URL);

        // send GET (Request)
        availableRides.add(oldRide);
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(oldRide.getRequest());
        outputStream.flush();
        outputStream.close();

        // handle OK (Data)
        // remove Ride from BookedRides
        // add Ride to LoadedRides
        // send GET (Ride)(Data)
        if (connection.getResponseCode() == 200) {
            String parsedData = ConnectionHelper.parseString(connection);
            oldRide.setData(parsedData);
            int i = bookedRides.indexOf(oldRide);
            loadedRides.add(bookedRides.remove(i));
        }

        sendGetRideRequestData(oldRide);




    }


    /**
     * # send GET (Ride)(Request)(Data)
     */
    @Override
    void sendGetRideRequestData(Ride oldRide) throws IOException {

        HttpURLConnection connection = ConnectionHelper.make("GET", MISP_BRIDGE_URL);

        // send GET (Ride)(Request)(Data)
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(oldRide.json());
        outputStream.flush();
        outputStream.close();

        // handle OK (EOL)
        // remove Ride from LoadedRides
        if (connection.getResponseCode() == 200) {
            Ride shellIdRide = ConnectionHelper.parseRide(connection);
            if (shellIdRide.getRideID() != null) {
                loadedRides.remove(oldRide);
            }
        }
    }

}