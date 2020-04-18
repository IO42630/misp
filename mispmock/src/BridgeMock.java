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


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {







        super.doPost(request,response);



    }



    /**
     * handle POST (Ride)
     * add Ride to AvailableRides
     */
    @Override
    protected Ride handlePostRide(HttpServletRequest request, HttpServletResponse response) {
        // WIP wait till GET (LINK) arrives.
        // # send OK (Ride) to mispclient
        return new Ride();
    }

}
