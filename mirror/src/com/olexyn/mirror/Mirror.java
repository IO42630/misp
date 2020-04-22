package com.olexyn.mirror;

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


    private final List<String> getList = new ArrayList<>();
    private final List<String> postList = new ArrayList<>();
    private final List<String> putList = new ArrayList<>();


    // #######
    //
    // #######

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        synchronized (getList) {
            getList.add(IOUtils.toString(request.getReader()));
        }

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
        synchronized (getList) {
            print.println(WebPrint.list(getList, "GET"));
        }
        print.println(" </body></html>");


    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        synchronized (postList) {
            postList.add(IOUtils.toString(request.getReader()));
        }
    }


    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException{
        synchronized (putList){
            putList.add(IOUtils.toString(request.getReader()));
        }
    }
}


