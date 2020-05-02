package com.olexyn.misp.mock.actor;



import com.olexyn.misp.mock.MockSet;
import com.olexyn.misp.mock.exchange.ExchangeMock;
import com.olexyn.misp.mock.exchange.RequestMock;
import org.apache.commons.io.IOUtils;

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


        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RequestMock mockRequest = (RequestMock) request;
        ExchangeMock exchange = mockRequest.exchange;

        synchronized (exchange) {
            String parsedRequest = IOUtils.toString(request.getReader());

            String dataString = "DATA-" + parsedRequest;

            exchange.response.setStatus(200);
            PrintWriter writer = exchange.response.getWriter();
            writer.write(dataString);
            writer.flush();
            writer.close();

            exchange.notify();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
