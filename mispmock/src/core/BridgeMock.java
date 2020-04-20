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

        RequestMock mockRequest = (RequestMock) request;
        ExchangeMock exchange = mockRequest.exchange;

        synchronized (exchange) {
            String parsedRequest = IOUtils.toString(request.getReader());
            JSONObject obj = new JSONObject(parsedRequest);
            parsedRequest = obj.getString("request");

            while (mapHelper.containsLess(1, State.AVAILABLE)) { Thread.sleep(Main.MOCK_SPEED); }


            Ride ride = null;
            for (int i = 0; ride ==null; i++){
                ride = mapHelper.pickAvailable();
                if (i!=0){
                Thread.sleep(Main.MOCK_SPEED);}
            }


            synchronized (ride) {
                ride.setRequest(parsedRequest);
                ride.setState(State.BOOKED);

                while (ride.getState() != State.LOADED) {
                    ride.notify();
                    Thread.sleep(Main.MOCK_SPEED);
                    ride.wait();
                }

                rideMap.remove(ride.getID());

                exchange.response.setStatus(200);
                PrintWriter writer = exchange.response.getWriter();
                writer.write(ride.getData());
                writer.flush();
                writer.close();

                ride.notify();
            }
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

        RequestMock mockRequest = (RequestMock) request;
        ExchangeMock exchange = mockRequest.exchange;

        synchronized (exchange) {
            String jsonPayload = IOUtils.toString(request.getReader());
            Ride parsedRide = new Ride(jsonPayload);
            Ride thisRide = rideMap.get(parsedRide.getID());

            synchronized (thisRide) {
                thisRide.setData(parsedRide.getData());
                thisRide.setState(State.LOADED);

                exchange.response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(thisRide.json());
                writer.flush();
                writer.close();

                thisRide.notify();
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

        RequestMock mockRequest = (RequestMock) request;
        ExchangeMock exchange = mockRequest.exchange;

        synchronized (exchange) {
            String jsonPayload = IOUtils.toString(request.getReader());
            Ride ride = new Ride(jsonPayload);

            synchronized (ride) {
                rideMap.put(ride.getID(), ride.setState(State.AVAILABLE));

                while (ride.getState() == State.AVAILABLE) {
                    ride.notify();
                    Thread.sleep(Main.MOCK_SPEED);
                    ride.wait();
                }
                exchange.response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(ride.json());
                writer.flush();
                writer.close();
                ride.notify();
            }
            exchange.notify();
        }
    }
}
