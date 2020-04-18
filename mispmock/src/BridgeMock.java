import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BridgeMock extends BridgeServlet {

    private MockSet mockSet;

    public BridgeMock(MockSet mockSet){
        super();
        mockSet.bridgeMock=this;
        this.mockSet = mockSet;
    }

    // #######
    //
    // #######

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request,response);
    }


    /**
     * handle GET (Link)
     * remove Ride from AvailableRides
     * add Ride to ReservedRides
     * send OK (Ride) to mispclient
     * send OK (Ride) to public
     */
    protected Ride handleGetLink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Ride ride = null;
        if(availableRides.size()>0){
            ride = availableRides.remove(0);
            reservedRides.add(ride);

        }
        // # send OK (Ride) to public
        return null;
    }


    /**
     * handle GET (Ride)
     * if Ride in ReservedRides,
     * remove Ride from ReservedRides
     * add Ride to Deliveredrides
     */
    protected Ride handleGetRide(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (Ride)(Request)
        return new Ride();
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
    protected Ride handleGetRideRequestData(HttpServletRequest request, HttpServletResponse response) {
        // # send OK (EOL)
        return new Ride();
    }

    // #######
    //
    // #######

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doPost(request,response);
    }


    /**
     * handle POST (Ride)
     * add Ride to AvailableRides
     */
    @Override
    protected Ride handlePostRide(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        // WIP wait till GET (LINK) arrives.
        String jsonPayload = IOUtils.toString(request.getReader());
        Ride ride = new Ride(jsonPayload);
        availableRides.add(ride);
        // WIP force surrender lock on ride
        int i = availableRides.indexOf(ride);
        availableRides.get(i).notify();
        // WIP wait to receive lock back
        availableRides.get(i).wait();

        // # send OK (Ride) to mispclient
        return null;
    }



}
