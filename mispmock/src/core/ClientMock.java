package core;

import exchange.ExchangeMock;
import org.json.JSONObject;

import javax.servlet.ServletException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Wraps a ClientServlet so it can be debugged easily, i.e. without running Tomcat.
 */
public class ClientMock extends ClientServlet {

    private MockSet mockSet;

    public ClientMock(MockSet mockSet){
        super();
        mockSet.clientMock = this;
        this.mockSet = mockSet;
    }

    @Override
    void sendPostRide(Ride ride) throws IOException, ServletException, InterruptedException {

        rideMap.put(ride.getID(), ride.setState(State.AVAILABLE));


        // Mock Exchange
        ExchangeMock exchange = new ExchangeMock();

        exchange.request.setMethod("POST");
        exchange.request.setContentType("application/json");
        exchange.request.setContent(ride.json().getBytes());

        synchronized (exchange){
            // Mock POST (Ride)
            exchange.notify();
            mockSet.bridgeMock.doPost(exchange.request,exchange.response);
            exchange.wait();

            // handle OK (Ride)(Request)
            Ride parsedRide = new Ride(exchange.response.getContentAsString());
            ride.setRequest(parsedRide.getRequest());

            exchange.notify();
        }
        ride.setState(State.BOOKED);
        sendGetRequest(ride);


    }


    /**
     * # send GET (Request) to App
     */
    @Override
    void sendGetRequest(Ride ride) throws IOException, ServletException, InterruptedException {

        // Mock Exchange
        ExchangeMock exchange = new ExchangeMock();

        exchange.request.setMethod("GET");
        exchange.request.setContentType("application/json");
        exchange.request.setContent(ride.json().getBytes());

        synchronized (exchange){
            // Mock GET (Request)
            exchange.notify();
            mockSet.appMock.doGet(exchange.request,exchange.response);
            //exchange.wait();

            // handle OK (Data)
            Ride parsedRide = new Ride(exchange.response.getContentAsString());
            ride.setData(parsedRide.getData());
            exchange.notify();
        }
        ride.setState(State.LOADED);
        sendGetRideRequestData(ride);

    }


    /**
     * # send GET (Ride)(Request)(Data)
     */
    @Override
    void sendGetRideRequestData(Ride ride) throws IOException, ServletException, InterruptedException {

        // Mock Exchange
        ExchangeMock exchange = new ExchangeMock();

        exchange.request.setMethod("GET");
        exchange.request.setContentType("application/json");
        exchange.request.setContent(ride.json().getBytes());

        synchronized (exchange){
            // Mock GET (Ride)(Request)(Data)
            exchange.notify();
            mockSet.bridgeMock.doGet(exchange.request,exchange.response);
            exchange.wait();

            // handle OK (Ride)
            Ride parsedRide = new Ride(exchange.response.getContentAsString());
            rideMap.remove(parsedRide.getID());
            exchange.notify();
        }
    }

}