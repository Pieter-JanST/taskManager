package com.taskmanager.controller;

import com.taskmanager.model.*;

import java.util.List;

import com.taskmanager.model.roles.Developer;
/**
 * GRASP controller for the Developer
 */
public class DevController {
    /**
     * The developer that is being controlled
     */
    private final Developer developer;

    /**
     * Constructor for the DevController
     * @param taskRepository The repository containing all tasks
     * @param userService The service containing all users
     * @param actionRepository The repository containing all actions
     * @param projectRepository The repository containing all projects
     */
    public DevController(TaskRepository taskRepository, UserService userService, ActionRepository actionRepository,ProjectRepository projectRepository) {
        developer = new Developer(taskRepository,userService,actionRepository, projectRepository);
    }


    /**
     * Extracts the task with id taskId from the repository
     * @param taskId The unique identifier of the task to be extracted from the repository
     * @return The task with unique id taskId, or null if no mapping to such exists
     */
    public Task getTaskById(Long taskId) {
        return developer.getTaskById(taskId);
    }


    /**
     * Extracts a list of every existing task
     * @return A list of all existing tasks inside the repository
     */
    public List<Task> getTasks() {
        return developer.getTasks();
    }


    /**
     * Ends the given task, and indicates that it either finished or failed
     * @param taskId The id of the task to be ended
     * @param failed FINISHED, or FAILED
     */
    public void endTask(long taskId, boolean failed,long userid) {
        developer.endTask(taskId, failed,userid);
    }


    /**
     * Adds a user to a task, if he is not yet working on one. If he is the system will present a
     * form in order to let the user switch tasks.
     * @param taskId The id of the task on which the user will start working
     * @param userById The user that will start working on the task
     * @return True if the user was already working on a task that is pending, False otherwise
     */
    public long addUserToTask(long taskId, long userById) {
        return developer.addUserToTask(taskId,userById);
    }
    public void switchTask(long taskId, long userById) {
        Task t = developer.getUserById(userById).getWorkingTask();
        String key = "";
        for (String id : t.getNecessaryRoles().keySet()) {

            if (t.getNecessaryRoles().get(id) != null && t.getNecessaryRoles().get(id).getId() == userById) {
                key = id;
            }

        }
        developer.removeUserFromTask(userById);
        developer.addUserToTask(taskId,userById,t,key);
    }


    /**
     * Creates and returns a list of
     * @param userId The user's id of which a list of tasks (s)he's working on should be returned
     * @return A list of all the tasks the user is currently working on
     */
    public List<Task> getWorkingTasks(long userId) {
        return developer.getWorkingTasks(userId);
    }

}
