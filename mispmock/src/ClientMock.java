import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    Ride sendPostRide(Ride ride) throws IOException, ServletException {
        // SUPER ILLEGAL MOCKING
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        mockSet.bridgeMock.doPost(request,response);
        return super.sendPostRide(ride);
    }



}