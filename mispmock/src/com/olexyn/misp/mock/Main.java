package com.olexyn.misp.mock;

import com.olexyn.misp.mock.actor.AppMock;
import com.olexyn.misp.mock.actor.UserMock;

public class Main {





    public  static void main(String... args){

        MockSet mockSet = new MockSet();

        Runnable publicRunnable = new UserMock(mockSet);
        Runnable bridgeRunable = new BridgeRunnable(mockSet);
        Runnable adapterRunnable = new AdapterRunnable(mockSet);
        //Runnable clientRunnable = new ClientRunnable(mockSet);
        Runnable appRunnable = new AppMock(mockSet);

        Thread userThread = new Thread(publicRunnable);
        Thread bridgeThread = new Thread(bridgeRunable);
        Thread adapterThread = new Thread(adapterRunnable);
        //Thread clientThread = new Thread(clientRunnable);
        Thread appThread = new Thread(appRunnable);

        userThread.setName("userThread");
        userThread.start();
        bridgeThread.setName("bridgeThread");
        bridgeThread.start();
        adapterThread.setName("adapterThread");
        adapterThread.start();
        //clientThread.setName("clientThread");
        //clientThread.start();
        appThread.setName("appThread");
        appThread.start();
    }
}



