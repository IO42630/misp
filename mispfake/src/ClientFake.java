import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;



public class ClientFake extends ServerFake {

    public ClientFake(){
        MainFake.clientFake = this;
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
                System.out.println("ClientFake.run() knows "+MainFake.bridgeFake.toString());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
