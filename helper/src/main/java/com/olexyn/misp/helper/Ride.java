package com.olexyn.misp.helper;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;


public class Ride {

    private static long count = 0L;

    @Getter
    private Long id;
    @Getter
    @Setter
    private String request;
    @Getter
    @Setter
    private String data;

    public Ride() { id = count++; }

    public Ride(String jsonString) { this(new JSONObject(jsonString)); }

    public Ride(JSONObject obj) {

        try { id = obj.getLong("id"); } catch (JSONException e) { id = count++; }

        try { request = obj.getString("request"); } catch (JSONException e) { request = null; }

        try { data = obj.getString("data"); } catch (JSONException e) { data = null; }
    }

    public String json() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("request", request);
        obj.put("data", data);
        return obj.toString();
    }
}


