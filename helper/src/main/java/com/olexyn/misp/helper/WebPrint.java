package com.olexyn.misp.helper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class WebPrint {


    public static String requestList(List<String> list) {

        StringBuilder sb = new StringBuilder();
        sb.append("<table style=\"width:100%\">");


        JSONObject obj = new JSONObject(list.get(0));
        Map<String, Object> map = obj.toMap();


        sb.append("<tr>");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append("<th>");
            sb.append(entry.getKey());
            sb.append("</th>");
        }
        sb.append("</tr>");


        for (String entry : list) {

            JSONObject line = new JSONObject(entry);
            Map<String, Object> map2 = line.toMap();

            sb.append("<tr>");
            for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
                sb.append("<td>");
                sb.append(entry2.getValue().toString());
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        return sb.toString();
    }
}