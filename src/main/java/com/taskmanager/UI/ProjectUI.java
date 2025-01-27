package com.taskmanager.UI;

import com.taskmanager.controller.PMController;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Class for handling the UI for the project
 */
@WebServlet("/project/*")
public class ProjectUI extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PMController pmController;
    @Override
    /**
     * Initializes the servlet
     */
    public void init() throws ServletException {
        pmController = (PMController) getServletContext().getAttribute("pmController");
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
     * Show the projectOverview page in the browser, this shows all the projects
     * @param request the request
     * @param response the response
     */
    protected void projectOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("projects", pmController.getProjectsList());
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/projectOverview.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Show the createProjectForm page in the browser, this allows the user to make a new project
     * @param request the request
     * @param response the response
     */
    protected void projectForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("projects", pmController.getProjectsList());
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/projectForm.jsp");
        dispatcher.forward(request, response);
    }
    /**
     * Add the created project
     * @param request the request
     * @param response the response
     */
    protected void createProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            long userid = (long) request.getSession().getAttribute("userId");

            pmController.addProject(request.getParameter("name"),request.getParameter("description"),request.getParameter("duetime"),userid);
            response.sendRedirect("/project/projectOverview?succesmessage=project%20has%20been%20added");
        } catch (Exception e){
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/projectForm.jsp?errormessage="+e.getMessage().replace(" ","%20"));
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the page for a specific project
     * @param request the request
     * @param response the response
     */
    protected void showProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("projectid"));
        request.setAttribute("project", pmController.getProjectsById(id));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/projectDetails.jsp");
        dispatcher.forward(request, response);

    }


}
