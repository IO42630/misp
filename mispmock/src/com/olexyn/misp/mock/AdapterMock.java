package com.olexyn.misp.mock;

import com.olexyn.misp.client.ClientServlet;
import com.olexyn.misp.helper.Ride;
import com.olexyn.misp.mock.exchange.ExchangeMock;
import com.olexyn.mispl.adapter.Adapter;

import javax.servlet.ServletException;
import java.io.IOException;


/**
 * Wraps a ClientServlet so it can be debugged easily, i.e. without running Tomcat.
 */
public class AdapterMock extends Adapter {

    private MockSet mockSet;


    public AdapterMock(MockSet mockSet) {
        super();
        mockSet.adapterMock = this;
        this.mockSet = mockSet;
    }

    /**
     * Send POST (Ride).
     * Parse response.
     */
    @Override
    protected Ride doSendPostRide(Ride ride) throws IOException, InterruptedException, ServletException {

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
    protected String doSendGetRequest(String request) throws IOException {

        // Mock Exchange
        final ExchangeMock exchange = new ExchangeMock();

        exchange.request.setMethod("GET");
        exchange.request.setContent(request.getBytes());

        synchronized (exchange) {
            // Mock GET (Request)
            exchange.notify();
            mockSet.appMock.doGet(exchange.request, exchange.response);

            // handle OK (Data)
            exchange.notify();
        }

        return exchange.response.getContentAsString();
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