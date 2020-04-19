import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Wraps a ClientServlet so it can be debugged easily, i.e. without running Tomcat.
 */
public class ClientMock extends ClientServlet  {

    private MockSet mockSet;

    public ClientMock(MockSet mockSet){
        super();
        mockSet.clientMock = this;
        this.mockSet = mockSet;
    }

    @Override
    void sendPostRide(Ride oldRide) throws IOException, ServletException {
        // SUPER ILLEGAL MOCKING
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setMethod("POST");
        request.setContentType("application/json");
        String payload = oldRide.json();
        request.setContent(payload.getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();
        availableRides.add(oldRide);
        mockSet.bridgeMock.doPost(request,response);
    }




}