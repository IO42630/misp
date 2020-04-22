package com.olexyn.misp.bridge;

import com.olexyn.misp.helper.Ride;

import java.util.Map;

public class Debug {
    
    
    public static String mapTables(BridgeServlet clientServlet){

        
        StringBuilder sb = new StringBuilder();
        sb.append(mapTable(clientServlet.available, "available"));
        sb.append(mapTable(clientServlet.booked, "booked"));
        sb.append(mapTable(clientServlet.loaded, "loaded"));
        return sb.toString();
    }


    private static String mapTable(Map<Long, Ride> foo, String type){
        StringBuilder sb = new StringBuilder();
        sb.append("<table style=\"width:100%\">");
        sb.append("<tr>");
        sb.append("<th>");
        sb.append("WebPrint: "+ type);
        sb.append("</th>");
        sb.append("</tr>");
        synchronized (foo){
            for(Map.Entry<Long,Ride> entry : foo.entrySet()){
                sb.append("<tr><td>"+entry.getValue()+"</td></tr>");
            }
        }
        sb.append("</table>");
        return  sb.toString();
    }
}
