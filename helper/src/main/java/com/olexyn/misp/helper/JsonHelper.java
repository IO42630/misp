package com.olexyn.misp.helper;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {

    public static boolean isJson(String string){
        try{
            new JSONObject(string);
        }catch (JSONException | NullPointerException e){
            return false;
        }
        return true;
    }
}
