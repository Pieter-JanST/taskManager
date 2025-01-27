package com.taskmanager.UI;

import com.taskmanager.controller.UserService;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Class for handeling the UI for the user
 */
@WebServlet("/user/*")
public class UserUI extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;


    @Override
    /**
     * Initializes the servlet
     */
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
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
     * @param request the request
     * @param response the response
     */
    protected void loginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles the request for the login page
     * @param request the request
     * @param response the response
     */
    protected void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        long userId = userService.logInUser(userName, password);

        if (userId == -1L) {
            request.setAttribute("errormessage", "Invalid userName or Password");
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        request.getSession().setAttribute("userId",userId);
        request.getSession().setAttribute("username",userName);
        request.getSession().setAttribute("roles",userService.getRolesFromUserId(userId));
        response.sendRedirect("/?succesmessage=succesfully%20logged%20in");
    }

    /**
     * Handles the request for the logout page
     * @param request the request
     * @param response the response
     */
    protected void logoutUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        request.setAttribute("succesmessage","succesfully logged out");
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
