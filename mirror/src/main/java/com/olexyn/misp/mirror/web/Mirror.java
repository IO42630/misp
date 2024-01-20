package com.olexyn.misp.mirror.web;

import com.olexyn.misp.helper.WebPrint;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Mirror {


    private final List<String> list = new ArrayList<>();


    private void addRequest(HttpServletRequest request) {
        synchronized (list) {
            JSONObject obj = new JSONObject();
            obj.put("RequestURL", request.getRequestURL());
            obj.put("RemoteAddr", request.getRemoteAddr());
            obj.put("Method", request.getMethod());
            obj.put("Param", request.getParameterMap().toString());
            obj.put("QueryString", request.getQueryString());
            obj.put("ContextPath", request.getContextPath());

            list.add(obj.toString());
        }
    }


    @GetMapping("/")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        addRequest(request);

        PrintWriter print = response.getWriter();

        print.println("<!DOCTYPE html>");
        print.println("<html lang=\"en\">");
        print.println("<head>");
        print.println("<meta charset=\"utf-8\">");
        print.println("<title>title</title>");
        print.println("<link rel=\"stylesheet\" href=\"style.css\">");
        print.println("<script src=\"script.js\"></script>");
        print.println("</head>");
        print.println("<body>");
        synchronized (list) { print.println(WebPrint.requestList(list)); }
        print.println(" </body></html>");
    }


    @PostMapping("/")
    public void doPost(HttpServletRequest request, HttpServletResponse response) { addRequest(request); }

}


