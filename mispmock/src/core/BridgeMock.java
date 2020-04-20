package core;

import exchange.ExchangeMock;
import exchange.RequestMock;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BridgeMock extends BridgeServlet {

    public BridgeMock(MockSet mockSet) {
        super();
        mockSet.bridgeMock = this;
    }


    /**
     * handle GET (Request)<br>
     * - wait for availableRides to have an entry <br>
     * - move move Ride to deliveredRides <br>
     * - wait for Ride to have Data <br>
     * - respond to the original request with Data
     */
    @Override
    protected void handleGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        final ExchangeMock exchange;
        final Ride ride;

        synchronized (exchange = ((RequestMock) request).exchange) {
            String parsedRequest = IOUtils.toString(request.getReader());
            final JSONObject obj = new JSONObject(parsedRequest);
            parsedRequest = obj.getString("request");

            synchronized (available) {

                while (available.size() < 1) {
                    available.notify();
                    available.wait();
                }
                // ride exists only locally, thus safe
                ride = available.entrySet().iterator().next().getValue();
                // ride exists only in "available", access through which is sync, thus safe
                available.remove(ride.getID());
                // needed because POST (Ride) wait()s
                available.notify();
            }

            synchronized (booked) {
                // ride exists only locally, thus safe
                booked.put(ride.getID(), ride);
                // ride exists only in "booked", access through which is sync, thus safe
                ride.setRequest(parsedRequest);
                // POST (Ride) wait()s
                booked.notify();
            }

            synchronized (loaded) {

                while (!loaded.containsKey(ride.getID())) {
                    loaded.notify();
                    loaded.wait();
                }


                // WIP this is tricky
                // what if ride exists in another map, e.g. "available'
                // in that case illegal access is possible
                // be carefull to removing ride from all other references, when adding it to "loaded".
                ride.setData(loaded.remove(ride.getID()).getData());
            }

            exchange.response.setStatus(200);
            final PrintWriter writer = exchange.response.getWriter();
            writer.write(ride.getData());
            writer.flush();
            writer.close();

            exchange.notify();
        }
    }


    /**
     * handle GET (Ride)(Data)
     * if Ride in ForwardedRequest
     * remove Ride from ForwardedRequest
     * add Ride to NewData
     * send OK (Ride)(Data)
     * remove Ride from NewData
     * add Ride to ForwardedData
     * send OK (EOL)
     * remove Ride from ForwardedData
     * add Ride to EOL
     */
    protected void handleGetRideRequestData(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        final ExchangeMock exchange;

        synchronized (exchange = ((RequestMock) request).exchange) {

            final String jsonPayload = IOUtils.toString(request.getReader());
            final Ride ride = new Ride(jsonPayload);

            synchronized (booked) {
                booked.remove(ride.getID());
            }

            synchronized (loaded) {
                loaded.put(ride.getID(), ride);
                loaded.notify();
            }
            exchange.notify();
        }

    }


    /**
     * handle POST (Ride)
     * - wait for Ride to be booked
     * - send OK to Client
     */
    @Override
    protected void handlePostRide(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        final ExchangeMock exchange;


        synchronized (exchange = ((RequestMock) request).exchange) {

            String jsonPayload = IOUtils.toString(request.getReader());
            final Ride ride = new Ride(jsonPayload);

            synchronized (available) {
                available.put(ride.getID(), ride);
                available.notify();
            }

            // ID is final/threadsafe
            while(!(booked.containsKey(ride.getID()))){

            }

            synchronized (booked) {
                //booked.notify();
                //booked.wait();
                ride.setRequest(booked.get(ride.getID()).getRequest());
            }

            exchange.response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(ride.json());
            writer.flush();
            writer.close();

            exchange.notify();
        }
    }
}
