package actor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import core.MockSet;

public class UserMock extends ActorRunnable {


    public UserMock(MockSet mockSet){
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
