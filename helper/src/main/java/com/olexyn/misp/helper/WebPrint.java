package com.olexyn.misp.helper;

import java.util.List;

public class WebPrint {

    final static public String SPLIT = "io32413445353";

    public static String list(List<String> list, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table style=\"width:100%\">");
        sb.append("<tr>");
        sb.append("<th>");
        sb.append("List: ");
        sb.append(type);
        sb.append("</th>");
        sb.append("</tr>");

        for (String entry : list) {
            sb.append("<tr><td>");
            sb.append(entry);
            sb.append("</td></tr>");
        }

        return sb.toString();
    }


    public static String requestList(List<String> list) {

        StringBuilder sb = new StringBuilder();
        sb.append("<table style=\"width:100%\">");
        sb.append("<tr>");
        sb.append("<th>");
        sb.append("URL");
        sb.append("</th>");
        sb.append("<th>");
        sb.append("Method");
        sb.append("</th>");
        sb.append("<th>");
        sb.append("Query");
        sb.append("</th>");
        sb.append("</tr>");

        for (String entry : list) {
            String[] split = entry.split(SPLIT);
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(split[0]);
            sb.append("</td>");
            sb.append("<td>");
            sb.append(split[1]);
            sb.append("</td>");
            sb.append("<td>");
            sb.append(split[2]);
            sb.append("</td>");
            sb.append("</tr>");
        }

        return sb.toString();
    }




}
