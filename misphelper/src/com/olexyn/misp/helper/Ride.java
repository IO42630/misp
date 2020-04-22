package com.olexyn.misp.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Ride {

    private static long count = 0L;

    final private Long id;
    private String request;
    private String data;

    // FUTURE it might be possible to use a ride for many requests.
    // private List<String> requests = new ArrayList<>();
    // private Map<String,String> data = new HashMap<>();


    public Ride() {
        id = count++;
    }

    public Ride(String jsonString) {

        JSONObject obj = new JSONObject();
        try {
            obj = new JSONObject(jsonString);
        }catch (JSONException e){
            int br = 0;
        }


        Long _id;

        try {
            _id = obj.getLong("id");
        }catch (JSONException e){
            _id = count++;
        }
        id = _id;
        try{
            request = obj.getString("request");
        } catch (JSONException e){
            request = null;
        }
        try{
            data = obj.getString("data");
        }catch (JSONException e){
            data = null;
        }


    }


    public void setRequest(String request) {
        this.request = request;
    }

    public void setData(String data) {
        this.data = data;
    }




    public String getRequest() {
        return this.request;
    }

    public String getData() {
        return this.data;
    }

    public Long getID() {
        return this.id;
    }



    private String brace(String foo) {
        return "\"" + foo + "\"";
    }

    private String unbrace(String foo) { return foo.replace("\"", ""); }

    public String json() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("request", request);
        obj.put("data",data);
        return obj.toString();
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


