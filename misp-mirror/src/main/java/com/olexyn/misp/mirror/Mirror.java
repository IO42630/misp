package com.olexyn.misp.mirror;

import com.olexyn.misp.helper.Ride;
import com.olexyn.misp.helper.WebPrint;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mirror extends HttpServlet {

    protected static final String MISP_CLIENT_URL = "http://localhost:9090/mispclient/core";


    private final List<String> list = new ArrayList<>();



    private void addRequest(HttpServletRequest request){
        synchronized (list) {
            StringBuffer sb = new StringBuffer();
            sb.append(request.getRequestURL().toString());
            sb.append(WebPrint.SPLIT);
            sb.append(request.getMethod());
            sb.append(WebPrint.SPLIT);
            sb.append(request.getQueryString());
            list.add(sb.toString());
        }
    }
    // #######
    //
    // #######

    @Override
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
        synchronized (list) {

            print.println(WebPrint.requestList(list));
        }




        print.println(" </body></html>");


    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)  {

        addRequest(request);
    }


    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response){
        addRequest(request);
    }
}


