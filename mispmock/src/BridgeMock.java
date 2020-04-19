import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

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

        while (!(availableRides.size() > 0)) { Thread.sleep(100); }

        Ride ride = availableRides.remove(0);
        deliveredRides.add(ride);

        while (ride.getData() == null) {
            ride.notify();
            ride.wait();
        }


        PrintWriter writer = response.getWriter();
        writer.print(ride.getData());


    }


    /**
     * handle GET (Ride)
     * if Ride in ReservedRides,
     * remove Ride from ReservedRides
     * add Ride to Deliveredrides
     */
    protected void handleGetRide(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (Ride)(Request)
        new Ride();
    }


    /**
     * handle GET (Ride)(Request)
     * if Ride in DeliveredRides
     * remove Ride from DeliveredRides
     * add Ride to NewRequest
     * send OK (Ride)(Request)
     * remove Ride from NewRequest
     * add Ride to ForwardedRequest
     */
    protected Ride handleGetRideRequest(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (Ride)(Request)(Data)
        return new Ride();
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
    protected void handleGetRideRequestData(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (EOL)
        new Ride();
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
     * add Ride to AvailableRides
     */
    @Override
    protected void handlePostRide(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        // WIP wait till GET (LINK) arrives.

        String jsonPayload = IOUtils.toString(request.getReader());
        Ride ride = new Ride(jsonPayload);
        availableRides.add(ride);
        synchronized (ride){

            while (availableRides.contains(ride)){
                ride.notify();
                Thread.sleep(100);
                ride.wait();
            }

            // SUPER ILLEGAL MOCKING
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.print(ride.json());
        }

    }


}
