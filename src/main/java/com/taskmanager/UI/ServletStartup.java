package com.taskmanager.UI;

import com.taskmanager.Seeder;
import com.taskmanager.controller.DevController;
import com.taskmanager.controller.PMController;
import com.taskmanager.controller.RoleController;
import com.taskmanager.controller.UserService;
import com.taskmanager.model.ActionRepository;
import com.taskmanager.model.ProjectRepository;
import com.taskmanager.model.TaskRepository;

import javax.servlet.*;

/**
 * Class for handling the startup of the servlet
 */
public class ServletStartup implements ServletContextListener {
        ServletContext servletContext;

        // Method 1
        @Override
        /**
         * Method for creating the servlet
         */
        public void contextInitialized(ServletContextEvent servletContextEvent) {

            // create all controllers and repos once !
            try {
                TaskRepository taskRepository = new TaskRepository();
                ProjectRepository projectRepository = new ProjectRepository();
                ActionRepository actionRepository = new ActionRepository();
                UserService userService = new UserService();
                PMController pmController = new PMController(projectRepository,taskRepository,actionRepository,userService);
                Seeder seeder = new Seeder(pmController,userService);
                seeder.seed();

                servletContext = servletContextEvent.getServletContext();
                servletContext.setAttribute("pmController", pmController);
                servletContext.setAttribute("devController", new DevController(taskRepository,userService,actionRepository,projectRepository));
                servletContext.setAttribute("roleController", new RoleController(userService,actionRepository,projectRepository,taskRepository));
                servletContext.setAttribute("userService", userService);



            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

}
