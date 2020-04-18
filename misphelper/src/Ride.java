import java.util.Objects;

public class Ride {

    private static long count = 0L;

    private String rideID;
    private String request;
    private String data;

    // FUTURE it might be possible to use a ride for many requests.
    // private List<String> requests = new ArrayList<>();
    // private Map<String,String> data = new HashMap<>();


    public Ride() {
        rideID = "" + count++;
    }

    public Ride(String json){
        json = json.replace("{","").replace("}","");
        String[] split = json.split(",");
        rideID = split[0].split(":")[1];
        request = split[1].split(":")[1];
        data = split[2].split(":")[1];
    }




    public void setRequest(String request) {
        this.request = request;
    }

    public void setData(String data){
        this.data = data;
    }

    public String getRequest(){
        return this.request;
    }

    public String getData(){
        return this.data;
    }

    public String getRideID(){
        return this.rideID;
    }

    private String brace(String foo){
        return "\""+foo+"\"";
    }

    public String json(){
        String[] keys = {"rideID", "request", "data"};
        String[] values = { rideID, request, data};
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i =0; i<keys.length; i++){
            sb.append(brace(keys[i]));
            sb.append(":");
            sb.append(brace(values[i]));
            if (i+1 < keys.length){
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
        return Objects.equals(rideID, ride.rideID) && Objects.equals(request, ride.request) && Objects.equals(data, ride.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rideID, request, data);
    }
}
