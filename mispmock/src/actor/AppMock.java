package actor;

import core.Main;
import core.MockSet;
import exchange.ExchangeMock;
import exchange.RequestMock;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AppMock extends ActorRunnable {

    public AppMock(MockSet mockSet) {
        super(mockSet);
        mockSet.appMock = this;
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Main.MOCK_SPEED);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RequestMock mockRequest = (RequestMock) request;
        ExchangeMock exchange = mockRequest.exchange;

        synchronized (exchange) {
            String parsedRequest = IOUtils.toString(request.getReader());
            JSONObject obj = new JSONObject(parsedRequest);
            parsedRequest = obj.getString("request");

            String dataString = "DATA-" + parsedRequest.split("-")[1];
            JSONObject dataObj = new JSONObject();
            dataObj.put("data", dataString);

            exchange.response.setStatus(200);
            PrintWriter writer = exchange.response.getWriter();
            writer.write(dataObj.toString());
            writer.flush();
            writer.close();

            exchange.notify();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
