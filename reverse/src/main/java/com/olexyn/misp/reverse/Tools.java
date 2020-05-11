package com.olexyn.misp.reverse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tools {


    public static String send(String method, String urlString, String body) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);


        boolean getToForward = method.equals("GET") && urlString.contains("forward");

        if (method.equals("POST") || getToForward) {
            connection.setDoOutput(true);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            if (body != null) { outputStream.writeBytes(body); }

            outputStream.flush();
            outputStream.close();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String out;
        StringBuilder sb = new StringBuilder();
        while ((out = in.readLine()) != null) { sb.append(out); }
        in.close();
        return sb.toString();
    }
}
