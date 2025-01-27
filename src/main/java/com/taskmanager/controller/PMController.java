package com.taskmanager.controller;


import com.taskmanager.model.*;
import com.taskmanager.model.roles.*;


import java.util.Collection;
import java.util.List;

/**
 * GRASP controller for the Project Manager
 */

public class PMController {

    /**
     * The project manager that is being controlled by this controller
     */
    private final ProjectManager projectManager;

    /**
     * Constructor for the project manager controller
     * @param projectRepository The repository containing all projects
     * @param taskRepository The repository containing all tasks
     * @param actionRepository The repository containing all actions
     * @param userService The service containing all users
     */
    public PMController(ProjectRepository projectRepository, TaskRepository taskRepository, ActionRepository actionRepository,UserService userService) {
        projectManager = new ProjectManager(projectRepository,taskRepository,actionRepository,userService);
    }


    /**
     * Creates and adds a new project to the projects of this project manager
     * @param name The name of the new project
     * @param desc The description of the new project
     * @param dueTime The due time of the new project
     * @return The unique id of the newly created project
     */
    public long addProject(String name, String desc, String dueTime,long userid) {
        return projectManager.addProject(name,desc,dueTime,userid);
    }


    /**
     * Returns the project with the given id, if it belongs to the projects of this project manager
     * @param id The unique id of the project to be returned
     * @return The project with the given id, or throws an error if this project does not exist in the project manager's repertoire
     */
    public Project getProjectsById(long id) {
        return projectManager.getProjectById(id);
    }


    /**
     * Returns a list of all existing projects inside the global repository
     * @return The collection of all projects
     */
    public Collection<Project> getProjectsList(){
        return projectManager.getProjectsList();
    }

    public List<Task> getAllTasks(){
        return projectManager.getAllTasks();
    }

    /**
     * Gets the tasks that belong to the given project
     * @param project The project of which all tasks should be returned
     * @return A list of all tasks of the project
     */
    public List<Task> getTasksFromProject(long project) {
        return getProjectsById(project).getTasks();
    }


    /**
     * Adds a task to a given project
     * @param projectid           The project id which the task should be added to
     * @param description         The description of the task
     * @param estimatedDuration   The estimated duration of the task
     * @param acceptableDeviation The acceptable deviation of the task
     * @param dependsOns          A list of task ids which this new task depends on
     * @return The unique id of the new task added to the project
     */
    public long addTask(long projectid, String description, int estimatedDuration, float acceptableDeviation, List<String>  role, List<Long> dependsOns,long userid) {
        return projectManager.addTask(projectid,description,estimatedDuration,acceptableDeviation,role,dependsOns,userid);
    }


    /**
     * Gets a task from a project via its id
     * @param projectId The id of the project
     * @param taskId The id of the task
     * @return The task corresponding to the id's
     */
    public  Task getTask(long projectId ,long taskId) {
        return projectManager.getTask(projectId, taskId);
    }


    /**
     * Replaces a given task via its id with a newly created one with the given variables
     * @param projectId The project to which the soon-to-be replaced task to
     * @param taskId The task which is to be replaced with a new one
     * @param estimated The estimated time parameter for the new task
     * @param deviation The acceptable deviation parameter for the new task
     * @param desc The description of the new task
     * @param roles The role(s) required for the new task
     * @param dependencies The dependencies of the new task
     */
    public void replaceTask(long projectId, long taskId, int estimated, float deviation, String desc,List<String> roles,List<Long> dependencies,long userid) {
        projectManager.replaceTask(projectId,taskId, estimated, deviation, desc,roles,dependencies,userid);
    }


    /**
     * Updates the task dependencies of the given task
     * @param taskId The task of which the dependencies should be updated
     * @param dependsOns A list of id's corresponding to the new dependencies of the given task
     */
    public void updateTaskDependencies(long taskId, List<Long> dependsOns, Long userId) {
        projectManager.updateTaskDependencies(taskId,dependsOns, userId);
    }


    /**
     * Gathers all the available tasks for a given project
     * @param projectId The project of which all available tasks should be returned
     * @param taskId The task of which all available tasks should be returned
     * @return A list of all available tasks of the given project
     */
    public List<Long> getAvailableTasks(long projectId, long taskId){
        List<Long> list = projectManager.getAvailableTasks(projectId);
        list.remove(taskId);
        return list;
    }
    /**
     * Gathers all the available tasks for all projects
     * @param taskid The task of which all available tasks should be returned
     * @return A list of all available tasks of the given project
     */
    public List<Long> getAllAvailableTasks(Long taskid){
        return projectManager.getAllAvailableTasks(taskid);
    }


    /**
     * Deletes a task
     * @param taskid The task to be deleted
     */
    public void deleteTask(long taskid) {
        projectManager.deleteTask(taskid);
    }

}
