package com.taskmanager.UI;

import com.taskmanager.controller.RoleController;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.IOException;

/**
 * Class for handling the UI for the actions
 */
@WebServlet("/action/*")
public class ActionUI extends HttpServlet {

    /**
     * The role controller
     */
    private RoleController roleController;

    @Override
    /**
     * Initializes the servlet
     */
    public void init() throws ServletException {
        roleController = (RoleController) getServletContext().getAttribute("roleController");
        super.init();
    }
    @Override
    /**
     * Handles the get requests
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UIInterFace.checkRequestUrl(this,request,response);
    }

    @Override
    /**
     * Handles the post requests
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UIInterFace.checkRequestUrl(this,request,response);
    }


    /**
     * Show the action page in the browser, where the user can see the actions
     * @param request the request
     * @param response the response
     */
    protected void actionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long userid = (long) request.getSession().getAttribute("userId");
        request.setAttribute("undoActions",roleController.getActionUndoList(userid));
        request.setAttribute("redoActions",roleController.getActionRedoList(userid));

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/actionOverview.jsp");
        dispatcher.forward(request, response);

    }

    /**
     * Undo the last action
     * @param request the request
     * @param response the response
     */
    protected void undo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            long userid = (long) request.getSession().getAttribute("userId");
            roleController.undoAction(userid);
            response.sendRedirect(" /action/actionPage?succesmessage=succesfully%20undo%20action");
        }catch (Exception e)
        {
            e.printStackTrace();
            response.sendRedirect(" /action/actionPage?errormessage="+e.getMessage().replace(" ","%20"));
        }
    }

    /**
     * Redo the last action
     * @param request the request
     * @param response the response
     */
    protected void redo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            long userid = (long) request.getSession().getAttribute("userId");
            roleController.redoAction(userid);
            response.sendRedirect(" /action/actionPage?succesmessage=succesfully%20redo%20action");
        }catch (Exception e)
        {
            e.printStackTrace();
            response.sendRedirect(" /action/actionPage?errormessage="+e.getMessage().replace(" ","%20"));
        }
    }
}
