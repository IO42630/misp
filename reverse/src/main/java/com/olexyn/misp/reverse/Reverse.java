package com.olexyn.misp.reverse;


import com.olexyn.misp.helper.Ride;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Reverse {


    private int available;
    public String FORWARD_URL = "http://localhost:8090/forward";
    public String APP_URL = "http://localhost:8090/app";


    public void start() throws IOException {

                    sendPostAvailable();
                    sendPostRide();


    }


    void sendPostRide() throws IOException {

        final Ride ride = new Ride();

        final String result = send("POST", FORWARD_URL, ride.json());






        String _req = new Ride(result).getRequest();
        String request = (_req == null) ? "" : _req;
        ride.setRequest(request);

        sendGetRequest(ride);
    }


    void sendGetRequest(Ride ride) throws IOException {

        final String result = send("GET", APP_URL, ride.getRequest());
        ride.setData(result);

        sendPostRideRequestData(ride);
    }


    void sendPostRideRequestData(Ride ride) throws IOException {

        send("POST", FORWARD_URL, ride.json());

        //sendPostRide();

    }


    void sendPostAvailable() throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("available", 0);
        String result = send("POST", FORWARD_URL, obj.toString());
        JSONObject resultObj = new JSONObject(result);
        available = resultObj.getInt("available");

    }




    private String send(String method, String urlString, String body) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);


        boolean getToForward = method.equals("GET") && urlString.contains("forward");

        if (method.equals("POST") || getToForward) {
            connection.setDoOutput(true);


            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            if (body != null) {
                outputStream.writeBytes(body);
            }

            outputStream.flush();
            outputStream.close();

        }


        int i = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String out;
        StringBuilder sb = new StringBuilder();
        while ((out = in.readLine()) != null) { sb.append(out); }
        in.close();
        return sb.toString();
    }


}


