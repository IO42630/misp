import exchange.ExchangeMock;
import exchange.RequestMock;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BridgeMock extends BridgeServlet {

    private MockSet mockSet;

    public BridgeMock(MockSet mockSet) {
        super();
        mockSet.bridgeMock = this;
        this.mockSet = mockSet;
    }

    // #######
    //
    // #######

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);
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

            while (!(availableRides.size() > 0)) { Thread.sleep(100); }

            Ride ride = availableRides.remove(0);

            synchronized (ride) {
                ride.setRequest(parsedRequest);
                bookedRides.add(ride);


                while (ride.getData()==null) {
                    ride.notify();
                    Thread.sleep(100);
                    ride.wait();
                }

                exchange.response.setStatus(200);
                PrintWriter writer = exchange.response.getWriter();
                writer.print(ride.getData());
                loadedRides.remove(ride);

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
            Ride ride = new Ride(jsonPayload);

            synchronized (ride) {
                bookedRides.remove(ride);

                while (!bookedRides.contains(ride)) {
                    ride.notify();
                    Thread.sleep(100);
                    ride.wait();
                }


                exchange.response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.print(ride.json());
                exchange.notify();
            }

        }

    }

    // #######
    //
    // #######

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doPost(request, response);
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
                availableRides.add(ride);
                while (!bookedRides.contains(ride)) {
                    ride.notify();
                    Thread.sleep(100);
                    ride.wait();
                }


                exchange.response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.print(ride.json());
                exchange.notify();
            }

        }


    }


}
