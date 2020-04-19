

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppMock extends ActorRunnable {

    public AppMock(MockSet mockSet){
        super(mockSet);
    }


    @Override
    public void run() {

    }

    @Override
    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



        //Main.clientRunnable.responses.add(mockResponse);

    }

    @Override
    void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
