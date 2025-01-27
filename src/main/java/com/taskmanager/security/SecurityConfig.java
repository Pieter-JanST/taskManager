package com.taskmanager.security;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * Class for handling the security configuration
 */
public class SecurityConfig {
    private static final Map<String, List<String>> roleConfig = new HashMap<String, List<String>>();

    /**
     * Initializes the security configuration
     */
    static {
        init();
    }

    /**
     * Checks if the user has the right permissions
     */
    private static void init() {

        // Dev urls
        List<String> developerUrls = new LinkedList<>();
        developerUrls.add("/task/taskForm");
        developerUrls.add("/task/taskOverview");
        developerUrls.add("/clock/timePage");
        developerUrls.add("/clock/timePage/");
        developerUrls.add("/clock/advanceTime");
        developerUrls.add("/task/startTask");
        developerUrls.add("/task/endTask");
        developerUrls.add("/user/logoutUser");
        developerUrls.add("/task/addUserToTask");
        developerUrls.add("/task/startTaskPage");
        developerUrls.add("/task/endTaskOverview");
        developerUrls.add("/task/endTaskPage");
        developerUrls.add("/task/showTask");
        developerUrls.add("/task/task/switchTask");
        developerUrls.add("/action/actionPage");
        developerUrls.add("/action/undo");
        developerUrls.add("/action/redo");


        // Man urls
        List<String> managerUrls = new LinkedList<>();
        managerUrls.add("/project/showProject");
        managerUrls.add("/clock/timePage");
        managerUrls.add("/clock/timePage/");
        managerUrls.add("/clock/advanceTime");
        managerUrls.add("/project/projectOverview");
        managerUrls.add("/user/logoutUser");
        managerUrls.add("/project/projectForm");
        managerUrls.add("/project/createProject");
        managerUrls.add("/task/taskOverview");
        managerUrls.add("/task/createTask");
        managerUrls.add("/task/taskForm");
        managerUrls.add("/task/showTask");
        managerUrls.add("/task/task/replaceTaskForm");
        managerUrls.add("/task/replaceTask");
        managerUrls.add("/task/updateTaskDependancies");
        managerUrls.add("/action/actionPage");
        managerUrls.add("/action/undo");
        managerUrls.add("/action/redo");
        managerUrls.add("/task/deleteTask");

        roleConfig.put(new String("Developer"), developerUrls);

        roleConfig.put(new String("ProjectManager"), managerUrls);
    }

    /**
     * Checks if the user has the right permissions
     * @param request the request
     * @param userRoles the user roles
     * @return true if the user has the right permissions, false otherwise
     */
    public static boolean hasPermissions(HttpServletRequest request, List<String> userRoles) {
        //String urlPattern = UrlPatternUtils.getUrlPattern(request);
        String url = request.getRequestURI();
        if (url.contains("login")){return true;}
        if (url.contains("logout")){return true;}
        if (url.contains("index")){return true;}
        if (url.equals("/")){return true;}

        Set<String> roles = roleConfig.keySet();
        for (String role : roles) {
            List<String> urls = roleConfig.get(role);
            if (urls != null && urls.contains(url) && userRoles.contains(role)) {

                return userRoles.contains(role);
            }
        }
        return false;
    }

}