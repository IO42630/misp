package actor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.MockSet;
import exchange.ExchangeMock;

/**
 * Generic Runnable.
 * Serves as basis for Actors that are not Servlets.
 */
public abstract class ActorRunnable implements Runnable {


    List<ExchangeMock> exchanges = new ArrayList<>();
    List<HttpServletRequest> requests = new ArrayList<>();

    protected MockSet mockSet;



    public ActorRunnable(MockSet mockSet){
        this.mockSet = mockSet;
    }




    @Override
    public void run() {
        while (true) {
            if (requests.size() > 0) {
                try {
                    processRequests(requests.remove(0));

                } catch (IOException | ServletException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void processRequests(HttpServletRequest request) throws IOException, ServletException {


        if (request.getMethod().equalsIgnoreCase("GET")) {
            doGet(request, null);
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            doPost(request, null);
        }
    }


    abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

}
