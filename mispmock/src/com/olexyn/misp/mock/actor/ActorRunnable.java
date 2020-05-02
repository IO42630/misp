package com.olexyn.misp.mock.actor;

import com.olexyn.misp.mock.MockSet;
import com.olexyn.misp.mock.exchange.ExchangeMock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Generic Runnable.
 * Serves as basis for Actors that are not Servlets.
 */
public abstract class ActorRunnable implements Runnable {


    List<ExchangeMock> exchanges = new ArrayList<>();

    protected MockSet mockSet;



    public ActorRunnable(MockSet mockSet){
        this.mockSet = mockSet;

    }




    @Override
    public void run() {
        //
    }


    public void processExchange(ExchangeMock exchange) throws IOException, ServletException {


        if (exchange.request.getMethod().equalsIgnoreCase("GET")) {
            doGet(exchange.request, exchange.response);
        } else if (exchange.request.getMethod().equalsIgnoreCase("POST")) {
            doPost(exchange.request, exchange.response);
        }
    }


    public abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    public abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

}
