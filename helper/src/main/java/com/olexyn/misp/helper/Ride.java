package com.olexyn.misp.helper;

import org.json.JSONException;
import org.json.JSONObject;


public class Ride {

    private static long count = 0L;

    final private Long id;
    private String request;
    private String data;


    public Ride() {
        id = count++;
    }

    public Ride(String jsonString) { this(new JSONObject(jsonString)); }

    public Ride(JSONObject obj) {

        long _id;
        try { _id = obj.getLong("id"); } catch (JSONException e) { _id = count++; }
        id = _id;

        try { request = obj.getString("request"); } catch (JSONException e) { request = null; }

        try { data = obj.getString("data"); } catch (JSONException e) { data = null; }
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


    public String json() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("request", request);
        obj.put("data", data);
        return obj.toString();
    }
}


