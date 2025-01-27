package com.taskmanager.UI;

import com.taskmanager.controller.PMController;
import com.taskmanager.controller.DevController;
import com.taskmanager.controller.RoleController;
import com.taskmanager.model.Task;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.*;

/**
 * Class for handling the UI for task
 */
@WebServlet("/task/*")
public class TaskUI extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PMController pmController;
    private DevController devController ;

    private RoleController roleController;
    @Override
    /**
     * Initializes the servlet
     */
    public void init() throws ServletException {
        pmController = (PMController) getServletContext().getAttribute("pmController");
        devController= (DevController) getServletContext().getAttribute("devController");
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
     * Show the taskOverview page in the browser, this allows the user to see all the tasks
     * @param request the request
     * @param response the response
     */
    protected void taskOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("tasks", devController.getTasks());
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/taskOverview.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Show the endTaskOverview page in the browser
     * @param request the request
     * @param response the response
     */
    protected void endTaskOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long userid = (long) request.getSession().getAttribute("userId");
        request.setAttribute("tasks", devController.getWorkingTasks(userid));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/endTaskOverview.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Show the createTaskForm page in the browser, this allows the user to make a new task
     * @param request the request
     * @param response the response
     */
    protected void taskForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("projects", pmController.getProjectsList());
            request.setAttribute("tasks", pmController.getAllTasks());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/taskForm.jsp");
            dispatcher.forward(request, response);
        }catch (Exception e){
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the createTaskForm page in the browser, this allows the user to make a new task
     * @param request
     * @param response
     */
    protected void createTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long projectid = Long.parseLong(request.getParameter("project"));
            int estimated = Integer.parseInt(request.getParameter("estimated"));
            float deviation = Integer.parseInt(request.getParameter("deviation"));
            String dependsOnsString = Arrays.toString(request.getParameterValues("dependsOn")).replace("[", "").replace("]", "");
            //No dependant tasks
            if(dependsOnsString.equals("")){
                long userid = (long) request.getSession().getAttribute("userId");
                pmController.addTask(projectid, request.getParameter("desc"),estimated,deviation, List.of(request.getParameter("rolesNecessary").split(",")), new LinkedList<Long>(),userid );
                response.sendRedirect("/project/showProject?projectid="+ projectid + "&succesmessage=task%20has%20been%20added");
                return;
            }
            //Get the id's of the dependant tasks from the string
            LinkedList<Long> dependsOns = new LinkedList<>();
            for (String s : dependsOnsString.split(",")) {
                if (!s.equals(" ")) {
                    dependsOns.add(Long.valueOf(s));
                }
            }
            long userid = (long) request.getSession().getAttribute("userId");

            Long taskId = pmController.addTask(projectid, request.getParameter("desc"),estimated,deviation, List.of(request.getParameter("rolesNecessary").split(",")), dependsOns,userid );
            response.sendRedirect("/project/showProject?projectid="+ projectid + "&succesmessage=task%20has%20been%20added");

        } catch (Exception e){
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/task/taskForm" /*?projectid="+ projectid*/);
            dispatcher.forward(request, response);
        }

    }

    /**
     * Show the page for a specific task
     * @param request the request
     * @param response the response
     */
    protected void showTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            long taskid = Long.parseLong(request.getParameter("taskid"));
            long projectid = Long.parseLong(request.getParameter("projectid"));

            request.setAttribute("task", pmController.getTask(projectid, taskid));
            List<Long> availableTasks = pmController.getAllAvailableTasks(taskid);
            availableTasks.remove(taskid);
            request.setAttribute("availableTasks",availableTasks);
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/taskDetails.jsp");
            dispatcher.forward(request, response);
        }catch (Exception e)
        {
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the page to end a task
     * @param request the request
     * @param response the response
     */
    protected void endTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long projectid = Long.parseLong(request.getParameter("projectid"));
            long taskid = Long.parseLong(request.getParameter("taskid"));
            boolean failed = Boolean.parseBoolean(request.getParameter("failed"));
            long userid = (long) request.getSession().getAttribute("userId");

            devController.endTask(taskid,failed,userid);
            response.sendRedirect("/task/taskOverview?succesmessage=succesfully%ended%20task&projectid="+projectid);
        }catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/task/taskOverview");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the page to replace a task
     * @param request the request
     * @param response the response
     */
    protected void replaceTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long projectid = Long.parseLong(request.getParameter("projectid"));
            long taskid = Long.parseLong(request.getParameter("taskid"));

            int estimated = Integer.parseInt(request.getParameter("estimated"));
            float deviation = Integer.parseInt(request.getParameter("deviation"));

            String[] dependsOnsString = request.getParameterValues("dependsOn");
            LinkedList<Long> dependsOns = new LinkedList<>();
            //als ge nie op de --- drukt bij depends on dan krijg je een nullpointer exception hier
            for (String s :dependsOnsString) {
                if (!s.equals("-1")){
                    if(pmController.getTask(projectid ,Long.valueOf(s))==null){
                        throw new IllegalArgumentException("invaild taskid");
                    };
                    dependsOns.add(Long.valueOf(s));
                }
            }
            long userid = (long) request.getSession().getAttribute("userId");

            pmController.replaceTask(projectid, taskid,estimated,deviation,request.getParameter("desc"),List.of(request.getParameter("rolesNecessary").split(",")),dependsOns,userid);
            response.sendRedirect(" /project/showProject?succesmessage=succesfully%20replaced%20task&projectid="+projectid);

        }catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/task/replaceTaskForm");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the page to replace a task
     * @param request the request
     * @param response the response
     */
    protected void replaceTaskForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long projectid = Long.parseLong(request.getParameter("projectid"));
            long taskid = Long.parseLong(request.getParameter("taskid"));
            List<Task> t = pmController.getTasksFromProject(projectid);
            t.remove(pmController.getTask(projectid,taskid));
            request.setAttribute("tasks",t);

            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/replaceTaskForm.jsp?projectid="+projectid+"&taskid="+taskid);
            dispatcher.forward(request, response);
        }catch (Exception e){
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the page to add a user to a task
     * @param request the request
     * @param response the response
     */
    protected void addUserToTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long taskid = Long.parseLong(request.getParameter("taskId"));
            long projectid = Long.parseLong(request.getParameter("projectId"));
            long userid = (long) request.getSession().getAttribute("userId");
            try {
                long pending = devController.addUserToTask(taskid,userid);
                if (pending != 0){
                    request.setAttribute("task", devController.getTaskById(taskid));
                    request.setAttribute("oldTask", devController.getTaskById(pending));

                    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/switchTaskPage.jsp");
                    dispatcher.forward(request, response);
                }else response.sendRedirect("/task/showTask?taskid="+ taskid+"&projectid="+projectid+"&succesmessage=succesfully%20work%20on%20task");

            } catch (IllegalArgumentException e){
                String error = e.getMessage().replace(" ","%20");
                response.sendRedirect("/task/showTask?taskid="+ taskid+"&projectid="+projectid+"&errormessage="+error);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/errorPage.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show theto update task dependancies
     * @param request the request
     * @param response the response
     */
    protected void updateTaskDependancies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long taskid = Long.parseLong(request.getParameter("taskId"));
            long projectid = Long.parseLong(request.getParameter("projectId"));
            String[] newDependancies = request.getParameterValues("newDependancies")[0].split(" ");
            List<Long> dependsOns = new ArrayList<>();
            if (!Objects.equals(newDependancies[0], "")) {
                for (String s : newDependancies) {
                    if (!s.equals(" ")) {
                        dependsOns.add(Long.valueOf(s));
                    }
                }
            }
            long userid = (long) request.getSession().getAttribute("userId");
            pmController.updateTaskDependencies(taskid,dependsOns, userid);
            response.sendRedirect("/task/showTask?taskid="+ taskid+"&projectid="+projectid+"&succesmessage=succesfully%20updated%20dependancies");
        }catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/errorPage.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the page to start a task
     * @param request the request
     * @param response the response
     */
    protected void startTaskPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long taskid = Long.parseLong(request.getParameter("taskid"));
            request.setAttribute("task", devController.getTaskById(taskid));
            request.setAttribute("time", roleController.getTime());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/startTaskPage.jsp");
            dispatcher.forward(request, response);
        }catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/errorPage.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * show the pagge to delete a task
     * @param request the request
     * @param response the response
     */
    protected void deleteTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long taskid = Long.parseLong(request.getParameter("taskid"));
            pmController.deleteTask(taskid);
            response.sendRedirect("/task/taskOverview?succesmessage=succesfully deleted task "+taskid);

        }catch (Exception e)
        {
            e.printStackTrace();
            response.sendRedirect("/task/taskOverview?errormessage="+ e.getMessage().replace(" ","%20"));
        }

    }

    /**
     * Show the page to end a task
     * @param request the request
     * @param response the response
     */
    protected void endTaskPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long taskid = Long.parseLong(request.getParameter("taskid"));
            request.setAttribute("task", devController.getTaskById(taskid));
            request.setAttribute("project", devController.getTaskById(taskid).getProject());
            request.setAttribute("time", roleController.getTime());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/endTaskPage.jsp");
            dispatcher.forward(request, response);

        }catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/errorPage.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Show the page to switch a task
     * @param request the request
     * @param response the response
     */
    public void switchTask (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            long taskid = Long.parseLong(request.getParameter("taskid"));
            long projectid = Long.parseLong(request.getParameter("projectid"));
            String roleId = request.getParameter("roleId");
            long userid = (long) request.getSession().getAttribute("userId");
            try {
                devController.switchTask(taskid,userid);
                response.sendRedirect("/task/showTask?taskid="+ taskid+"&projectid="+projectid+"&succesmessage=succesfully%20switched%20task");
            } catch (IllegalArgumentException e){
                String error = e.getMessage().replace(" ","%20");
                response.sendRedirect("/task/showTask?taskid="+ taskid+"&projectid="+projectid+"&errormessage="+error);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/errorPage.jsp");
            dispatcher.forward(request, response);
        }
    }

}
