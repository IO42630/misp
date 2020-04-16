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


public final class ClientServlet extends HttpServlet {

    private static final String MISP_BRIDGE_URL = "http://localhost:9090/mispbridge/core";



    public ClientServlet() {


    }

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

        writer.println(make2ColumnRow("sendGet", sendGet()));
        writer.println(make2ColumnRow("sendPost", sendPost()));


        writer.println("</table>");
        writer.println("</body>");
        writer.println("</html>");
    }


    private String make2ColumnRow(String one, String two){
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



    private String sendPost() throws IOException{

        URL url = new URL(MISP_BRIDGE_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "USER_AGENT");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        String postJsonData = "{\"id\":5,\"countryName\":\"USA\",\"population\":8000}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postJsonData);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + postJsonData);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        //printing result from response
        System.out.println();



        return response.toString();
    }

}
