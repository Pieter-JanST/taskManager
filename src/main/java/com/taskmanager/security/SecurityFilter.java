package com.taskmanager.security;

import com.taskmanager.controller.UserService;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for handling the security
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {

    /**
     * Constructor
     */
    public SecurityFilter() {

    }

    @Override
    /**
     * Destroys the filter
     */
    public void destroy() {
    }

    @Override
    /**
     * Handles the filter
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        UserService userService = (UserService) request.getServletContext().getAttribute("userService");

        String servletPath = request.getServletPath();

        if (servletPath.contains("css")) {
            chain.doFilter(request, response);
            return;
        }

        List<String> userRoles = new ArrayList<>();
        if (request.getSession().getAttribute("userId") != null){
            long id = (long) request.getSession().getAttribute("userId");
            userRoles = userService.getRolesFromUserId(id);
        }




        if (!SecurityConfig.hasPermissions(request,userRoles)) {
            request.setAttribute("errormessage","No Access");
            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/errorPage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    /**
     * Initializes the filter
     */
    public void init(FilterConfig fConfig) throws ServletException {

    }

}