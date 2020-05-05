package com.olexyn.misp.embedded;

import com.olexyn.min.http.server.MinJettyServer;
import com.olexyn.misp.forward.Forward;
import com.olexyn.misp.mirror.Mirror;


public class Embedded implements Runnable {


    @Override
    public void run() {
        MinJettyServer server = new MinJettyServer();

        server.PORT = 8090;
        server.MAX_THREADS = 100;
        server.MIN_THREADS = 10;
        server.IDLE_TIMEOUT = 120;


        server.addServletWithMapping("/mirror", Mirror.class);
        server.addServletWithMapping("/app", Mirror.class);
        server.addServletWithMapping("/forward", Forward.class);


        server.start();
    }
}
