import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionHelper {


    public static HttpURLConnection make(String method, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", "USER_AGENT");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }


    public static Ride parseRide(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String out;
        StringBuilder sb = new StringBuilder();
        while ((out = in.readLine()) != null) { sb.append(out); }
        in.close();
        return new Ride(sb.toString());
    }
}
