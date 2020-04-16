//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public final class ClientServlet extends HttpServlet {

    private static final String MISP_BRIDGE_URL = "http://localhost:9090/mispbridge/core";

    private List<Ride> availableRides = new ArrayList<>();
    private List<Ride> reservedRides = new ArrayList<>();
    private List<Ride> newRequests = new ArrayList<>();
    private List<Ride> forwardedRequests = new ArrayList<>();
    private List<Ride> newData = new ArrayList<>();
    private List<Ride> forwardedData = new ArrayList<>();


    public ClientServlet() {

        // Thread 1
        // get ride count from mispbridge
        // while ride count < max
        // add POST (Ride) to AvailableRides
        // send POST (Ride)


        // Thread 2
        // for Rides in ReservedRides,
        // send GET (Ride)

    }

    // handle OK (Ride)
    // remove Ride from AvailableRides
    // add Ride to ReservedRides

    // handle OK (Ride)(Request)
    // remove Ride from AvailableRides
    // add Request to NewRequest
    // send GET (Request)
    // remove Ride from NewRequest
    // add Ride to ForwardedRequest

    // handle OK (Data)
    // remove Ride from ForwardedRequest
    // add Ride to NewData
    // send GET (Ride)(Data)
    // remove Ride from NewData
    // add Ride to ForwardedData

    // handle OK (EOL)
    // remove Ride from ForwardedData
    // add Ride to EOl


    // # send POST (Ride)

    // # send GET (Ride)

    // # send GET (Request)

    // # send GET (Ride)(Data)


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>MispClient</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=white>");
        writer.println("<table border=\"0\">");
        writer.println("<tr>");
        writer.println("<td>");
        writer.println("<img src=\"images/tomcat.gif\">");
        writer.println("</td>");
        writer.println("<td>");
        writer.println("<h1>Sample Application Servlet</h1>");
        writer.println("This is the output of a servlet that is part of");
        writer.println("the Hello, World application.");
        writer.println("</td>");
        writer.println("</tr>");
        writer.println(make2ColumnRow("foo", "bar"));


        writer.println("</table>");
        writer.println("</body>");
        writer.println("</html>");
    }


    private String make2ColumnRow(String one, String two) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td>");
        sb.append(one);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(two);
        sb.append("</td>");
        sb.append("</tr>");

        return sb.toString();
    }


    private String sendGet() throws IOException {


        URL url = new URL(MISP_BRIDGE_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // By default it is GET request
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "USER_AGENT");

        int responseCode = con.getResponseCode();
        System.out.println("Sending get request : " + url);
        System.out.println("Response code : " + responseCode);

        // Reading response from input Stream
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();


        return response.toString();
        //printing result from response


    }


    private void sendPostRide() throws IOException {

        URL url = new URL(MISP_BRIDGE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // prepare
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "USER_AGENT");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/json");

        Ride ride = new Ride();
        availableRides.add(ride);


        // send POST
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(ride.json());
        outputStream.flush();
        outputStream.close();


        // read OK
        int responseCode = connection.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();


        // process
        availableRides.remove(ride);
        reservedRides.add(ride);
    }


    private void sendGetRide(Ride ride) throws IOException {

        URL url = new URL(MISP_BRIDGE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // prepare
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "USER_AGENT");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/json");




        // send POST
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(ride.json());
        outputStream.flush();
        outputStream.close();


        // read OK
        int responseCode = connection.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();


        Ride rideRequest = new Ride(response.toString());


        // process
        // TODO add checks
        reservedRides.remove(ride);
        newRequests.add(rideRequest);
        sendGetRequest(rideRequest.getRequest());
        newRequests.remove(rideRequest);
        forwardedRequests.add(rideRequest);


    }

    private void sendGetRequest(String request) throws IOException {

        URL url = new URL(MISP_BRIDGE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // prepare
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "USER_AGENT");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/json");


        // TODO replace
        Ride ride = new Ride();

        // send POST
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(ride.json());
        outputStream.flush();
        outputStream.close();


        // read OK
        int responseCode = connection.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();


        Ride rideRequest = new Ride(response.toString());


        // process
        // TODO add checks
        reservedRides.remove(ride);
        newRequests.add(ride);
    }


}
