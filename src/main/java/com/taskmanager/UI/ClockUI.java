package com.taskmanager.UI;

import com.taskmanager.controller.PMController;
import com.taskmanager.controller.RoleController;
import com.taskmanager.model.Clock;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Class for handling the UI for the clock
 */
@WebServlet("/clock/*")
public class ClockUI extends HttpServlet {

    /**
     * The ProjectManagerController
     */
    private PMController pmController;
    /**
     * The roleController
     */
    private RoleController roleController;

    @Override
    /**
     * Initializes the servlet
     */
    public void init() throws ServletException {
        pmController = (PMController) getServletContext().getAttribute("pmController");
        roleController = (RoleController) getServletContext().getAttribute("roleController");
        super.init();
    }
    @Override
    /**
     * Handles the get requests
     * @param request the request
     * @param response the response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UIInterFace.checkRequestUrl(this,request,response);
    }

    @Override
    /**
     * Handles the post requests
     * @param request the request
     * @param response the response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UIInterFace.checkRequestUrl(this,request,response);
    }

    /**
     * Show the time page in the browser, where the user can adjust the system time
     * @param request the request
     * @param response the response
     */
    protected void advanceTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Timestamp t = null;
            if (request.getParameter("systemTime") == null ) {
                long m = Integer.valueOf(request.getParameter("minutesTime"))*60*1000;
                t = new Timestamp(Clock.getSystemTime().getTime() + m);
            }else{
                t = Timestamp.valueOf(request.getParameter("systemTime").replace("T", " "));
            }
            roleController.advanceTime(t);

            response.sendRedirect("/clock/timePage/?succesmessage=succesfully%20adjusted%20systemTime");
        }catch (Exception e){
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/clock/timePage");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the time page in the browser
     * @param request the request
     * @param response the response
     */
    protected void timePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("clock",roleController.getTime());
        request.setAttribute("clockValue",roleController.getTime().toString().replace(" ","T").split("\\.")[0]);

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/timePage.jsp");
        dispatcher.forward(request, response);
    }
}
