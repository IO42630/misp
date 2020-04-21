package core;

import exchange.ExchangeMock;

import javax.servlet.ServletException;
import java.io.IOException;


/**
 * Wraps a ClientServlet so it can be debugged easily, i.e. without running Tomcat.
 */
public class ClientMock extends ClientServlet {

    private MockSet mockSet;


    public ClientMock(MockSet mockSet) {
        super();
        mockSet.clientMock = this;
        this.mockSet = mockSet;
    }

    /**
     * Send POST (Ride).
     * Parse response.
     */
    @Override
    protected Ride doSendPostRide(Ride ride) throws IOException, ServletException, InterruptedException {

        // Mock Exchange
        final ExchangeMock exchange = new ExchangeMock();
        exchange.request.setMethod("POST");
        exchange.request.setContentType("application/json");
        exchange.request.setContent(ride.json().getBytes());

        synchronized (exchange) {
            // Mock POST (Ride)
            exchange.notify();
            mockSet.bridgeMock.doPost(exchange.request, exchange.response);
            exchange.wait();
            exchange.notify();
        }

        // handle OK (Ride)(Request)
        return new Ride(exchange.response.getContentAsString());
    }


    /**
     * Send GET (Request) to App.
     * Parse response.
     */
    @Override
    protected Ride doSendGetRequest(Ride ride) throws IOException, InterruptedException {

        // Mock Exchange
        final ExchangeMock exchange = new ExchangeMock();

        exchange.request.setMethod("GET");
        exchange.request.setContentType("application/json");
        exchange.request.setContent(ride.json().getBytes());

        synchronized (exchange) {
            // Mock GET (Request)
            exchange.notify();
            mockSet.appMock.doGet(exchange.request, exchange.response);

            // handle OK (Data)
            exchange.notify();
        }

        return new Ride(exchange.response.getContentAsString());
    }


    /**
     * Send GET (Ride)(Request)(Data).
     * Parse response.
     */
    @Override
    protected void doSendGetRideRequest(Ride ride) throws IOException, InterruptedException {

        // Mock Exchange
        final ExchangeMock exchange = new ExchangeMock();
        exchange.request.setMethod("GET");
        exchange.request.setContentType("application/json");
        exchange.request.setContent(ride.json().getBytes());

        synchronized (exchange) {
            // Mock GET (Ride)(Request)(Data)
            exchange.notify();
            mockSet.bridgeMock.doGet(exchange.request, exchange.response);
            exchange.wait();
            exchange.notify();
        }
    }

}