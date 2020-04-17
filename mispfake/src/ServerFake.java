import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;



public abstract class ServerFake implements Runnable {


    List<HttpServletResponse> responses = new ArrayList<>();
    List<HttpServletRequest> requests = new ArrayList<>();





    @Override
    public abstract void run();


    public void process(HttpServletRequest request,HttpServletResponse response) throws IOException {
       if (request.getMethod().equalsIgnoreCase("GET")){
            doGet(request,response);
        } else if(request.getMethod().equalsIgnoreCase("POST")){
           doPost(request,response);
       }
    }



    abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;

    abstract void doPost(HttpServletRequest request, HttpServletResponse response);

}
