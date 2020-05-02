package com.olexyn.misp.mock;


import com.olexyn.misp.mock.exchange.ExchangeMock;
import com.olexyn.misp.mock.exchange.RequestMock;
import com.olexyn.misp.bridge.BridgeServlet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        synchronized (exchange = ((RequestMock) request).exchange) {

            super.handleGetRequest(request,response);

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

            super.handleGetRideRequestData(request,response);

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

            super.handlePostRide(request,response);

            exchange.notify();
        }
    }
}
