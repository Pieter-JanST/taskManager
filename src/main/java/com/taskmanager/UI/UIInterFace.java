package com.taskmanager.UI;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Interface for the UI
 */
public interface UIInterFace {
    /**
     * Checks the request url and calls the corresponding method
     * @param o The object to call the method on
     * @param request The request
     * @param response The response
     */
    static void checkRequestUrl(Object o, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] route = request.getRequestURI().split("/");
        try {
            Method m = o.getClass().getDeclaredMethod(route[route.length-1], HttpServletRequest.class, HttpServletResponse.class);
            m.invoke(o,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errormessage","No Acces");
            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/errorPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}
