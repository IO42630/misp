import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PublicMock extends ActorRunnable {


    public PublicMock(MockSet mockSet){
        super(mockSet);
    }

    @Override
    public void run() {

    }

    @Override
    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}