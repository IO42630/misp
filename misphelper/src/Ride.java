import java.util.Objects;

public class Ride {

    private static long count = 0L;

    private Long id;
    private String request;
    private String data;
    private State state;

    // FUTURE it might be possible to use a ride for many requests.
    // private List<String> requests = new ArrayList<>();
    // private Map<String,String> data = new HashMap<>();


    public Ride() {
        id = count++;
    }

    public Ride(String json) {
        json = json.replace("{", "").replace("}", "");
        String[] split = json.split(",");
        String idString = unbrace(split[0].split(":")[1]);
        id = Long.parseLong(idString);
        request = unbrace(split[1].split(":")[1]);
        data = unbrace(split[2].split(":")[1]);
    }


    public void setRequest(String request) {
        this.request = request;
    }

    public void setData(String data) {
        this.data = data;
    }


    public Ride setState(State state){ this.state = state; return this;}

    public String getRequest() {
        return this.request;
    }

    public String getData() {
        return this.data;
    }

    public Long getID() {
        return this.id;
    }

    public State getState(){return this.state;}

    private String brace(String foo) {
        return "\"" + foo + "\"";
    }

    private String unbrace(String foo){ return foo.replace("\"","");    }

    public String json() {
        String[] keys = {"rideID",
                         "request",
                         "data"};
        String[] values = {"" + id,
                           request,
                           data};
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < keys.length; i++) {
            sb.append(brace(keys[i]));
            sb.append(":");
            sb.append(brace(values[i]));
            if (i + 1 < keys.length) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return Objects.equals(id, ride.id) && Objects.equals(request, ride.request) && Objects.equals(data, ride.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, request, data);
    }




}


