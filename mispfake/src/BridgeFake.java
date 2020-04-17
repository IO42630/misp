import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.util.stream.Collectors;

public class BridgeFake extends ServerFake {

    public BridgeFake(){
        MainFake.bridgeFake = this;
    }


    @Override
    public void run() {
        while (true){
            if (requests.size()>0 && responses.size()>0) {
                try {
                    process(requests.remove(0), responses.remove(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                System.out.println("BridgeFake.run() knows "+MainFake.clientFake.toString());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));


        if (body.contains("LINK")) {

        } else {
            Ride suppliedRide = new Ride(body);
            if (suppliedRide.getRideID() != null) {
                sendDummy(response);
            } else if (suppliedRide.getRequest() != null) {
                sendDummy(response);
            } else if (suppliedRide.getData() != null) {
                sendDummy(response);
            } else {
                // ERROR
            }
        }
    }

    @Override
    void doPost(HttpServletRequest request, HttpServletResponse response) {

    }



    private void sendDummy(HttpServletResponse response) throws IOException {
        Ride dummyRide = new Ride();
        dummyRide.setRequest("dummyRequest");
        dummyRide.setData("dummyData");


        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);


        response.setStatus(200);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println(dummyRide.json());
    }
}
