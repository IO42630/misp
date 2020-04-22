package com.olexyn.misp.client;

import com.olexyn.misp.helper.Ride;

import java.util.Map;

public class Debug {
    
    
    public static String mapTables(ClientServlet clientServlet){

        
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>WebPrint:</h1>");
        sb.append(mapTable(clientServlet.available, "available"));
        sb.append(mapTable(clientServlet.booked, "booked"));
        sb.append(mapTable(clientServlet.loaded, "loaded"));
        return sb.toString();
    }


    private static String mapTable(Map<Long,Ride> foo, String type){
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>"+type+"</h2>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<th>");
        sb.append("ID");
        sb.append("</th>");
        sb.append("<th>");
        sb.append("Request");
        sb.append("</th>");
        sb.append("<th>");
        sb.append("Data");
        sb.append("</th>");
        sb.append("</tr>");
        synchronized (foo){
            for(Map.Entry<Long, Ride> entry : foo.entrySet()){
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(entry.getValue().getID());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(entry.getValue().getRequest());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(entry.getValue().getData());
                sb.append("</td>");
                sb.append("</tr>");
            }
        }
        sb.append("</table>");
        return  sb.toString();
    }
}
