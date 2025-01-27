package com.taskmanager.model.roles;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;
import java.sql.Timestamp;
import java.util.*;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;

public class ProjectManager {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ActionRepository actionRepository;

    /**
     A list to keep track of the project ID's of this project manager
     */
    private static final ArrayList<Long> projectIds = new ArrayList<>();

    /**
     * A map containing the project ID's and the task ID's.
     */
    private Map<Long, Long> projectAndTasks = new HashMap<>();

    public ProjectManager(ProjectRepository projectRepository,TaskRepository taskRepository,ActionRepository actionRepository,UserService userService){
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.actionRepository = actionRepository;
    }

    /**
     * Creates and adds a new project to the projects of this project manager
     * @param name The name of the new project
     * @param desc The description of the new project
     * @param dueTime The due time of the new project
     * @return The unique id of the newly created project
     */
    public long addProject(String name, String desc, String dueTime,long userid) {
        Timestamp t;
        if (dueTime.contains("T")) {
            t = Timestamp.valueOf(dueTime.replace("T"," ")+":00");
        }else{
            t = Timestamp.valueOf(dueTime);
        }
        Timestamp original = getOriginalSystemTime();
        Timestamp systemTime = getSystemTime();
        Project p = new Project(name,desc,t, systemTime, original);
        this.projectRepository.addProject(p);
        projectIds.add(p.getId());


        actionRepository.addAction(
                new Action("added Project "+p.getName(),userid,()->{

                    projectIds.remove(p.getId());
                    projectRepository.deleteProject(p);
                },()->{
                    projectIds.add(p.getId());
                    projectRepository.addProject(p);
                }));

        return p.getId();
    }


    /**
     * Returns the project with the given id, if it belongs to the projects of this project manager
     * @param id The unique id of the project to be returned
     * @return The project with the given id, or throws an error if this project does not exist in the project manager's repertoire
     */
    public Project getProjectById(Long id){
        if (projectIds.contains(id)){return this.projectRepository.getProjectsById(id);}
        else {throw new IllegalArgumentException("Project with this id was not found inside this project manager's projects");}
    }


    /**
     * Adds a task to a given project
     * @param projectId           The project which the task should be added to
     * @param description         The description of the task
     * @param estimatedDuration   The estimated duration of the task
     * @param acceptableDeviation The acceptable deviation of the task
     * @param dependsOns          A list of task ids which this new task depends on
     * @return The unique id of the new task added to the project
     */
    public long addTask(long projectId, String description, int estimatedDuration, float acceptableDeviation,List<String> role, List<Long> dependsOns,long userid)  {

        Project p = projectRepository.getProject(projectId);
        if (p == null){throw new IllegalArgumentException("No project was found with the given id");}
        ArrayList<Task> newDependencies = new ArrayList<>();
        for(Long d: dependsOns) {
            if(!this.taskRepository.exists(d)){throw new IllegalArgumentException("Task "+ d +" does not exist in the repository");}
            newDependencies.add(taskRepository.getTaskById(d));
        }
        Timestamp time = getSystemTime();
        Task t = new Task(description,estimatedDuration,acceptableDeviation,p, time, role, newDependencies);



        this.taskRepository.addTask(t);
        this.projectRepository.addProject(p);

        try {

            actionRepository.addAction(new Action("added Task " + t.getDescription(), userid, ()->{
                this.taskRepository.deleteTask(t.getId());

            }, ()->{
                this.taskRepository.addTask(t);

            }));
            projectAndTasks.put(projectId, t.getId());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return t.getId();
    }


    /**
     * Gets a task from a project via its id
     * @param projectId The id of the project
     * @param taskId The id of the task
     * @return The task corresponding to the id's
     */
    public Task getTask(long projectId, long taskId) {
        if(projectIds.contains(projectId)){ return this.projectRepository.getProjectsById(projectId).getTaskById(taskId);}
        else {throw new IllegalArgumentException("The project with this id was not found in this project manager's repertoire");}
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

        ArrayList<Task> newDependencies = new ArrayList<>();
        for (Long id: dependencies) {
            newDependencies.add(projectRepository.getTask(projectId,id));
        }
        Timestamp time = getSystemTime();
        Task t = projectRepository.getTask(projectId, taskId);

        String d = t.getDescription();
        int e = t.getEstimatedDuration();
        float a = t.getAcceptableDeviation();
        List<String> r  = new ArrayList<>();

        for (String s :t.getNecessaryRoles().keySet()) {
            r.add(s.replaceAll("[0-9]",""));
        }

        Timestamp ti = t.getTimeSpan().getStartTime();
        ArrayList<Task> dep = t.getDependsOn();
        projectRepository.getTask(projectId, taskId).replaceTask(desc,estimated,deviation,roles, time, newDependencies);
        actionRepository.addAction(new Action("replace Task",userid,
                ()->{
                    throw new AssertionError();//projectRepository.getTask(projectId, taskId).replaceTask(d,e,a,r,ti,dep);
                },()->{
                projectRepository.getTask(projectId, taskId).replaceTask(desc,estimated,deviation,roles, time, newDependencies);

        }));
    }


    /**
     * Updates the task dependencies of the given task
     * @param taskId The task of which the dependencies should be updated
     * @param newDependenciesId A list of id's corresponding to the new dependencies of the given task
     */
    public void updateTaskDependencies(long taskId, List<Long> newDependenciesId, long userid){
        ArrayList<Long> oldDependenciesId = new ArrayList<>();
        for (Task t: this.taskRepository.getTaskById(taskId).getDependsOn()) {
            oldDependenciesId.add(t.getId());
        }
        this.taskRepository.updateTaskDependencies(taskId,newDependenciesId);

        actionRepository.addAction(new Action("updated dependencies",userid,
                () -> {
                    this.taskRepository.updateTaskDependencies(taskId, oldDependenciesId);
                },() -> {
                    this.taskRepository.updateTaskDependencies(taskId,newDependenciesId);
                }));
    }


    /**
     * Gathers all the available tasks for a given project
     * @param projectId The project of which all available tasks should be returned
     * @return A list of all available tasks of the given project
     */
    public List<Long> getAvailableTasks(long projectId){
        List<Long> availableTasks = new ArrayList<>();
        for (Task t: this.projectRepository.getProjectsById(projectId).getTasks()) {
            if (t.getStatus().equals("AVAILABLE") || t.getStatus().equals("UNAVAILABLE")){
                availableTasks.add(t.getId());
            }
        }
        return availableTasks;
    }

    /**
     * Gathers all the available tasks from all the projects
     * @return A list of all available tasks
     */
    public List<Long> getAllAvailableTasks(Long taskId){
        List<Long> availableTasks = new ArrayList<>();
        ArrayList<Task> dependsOn = taskRepository.getTaskById(taskId).getDependsOn();
        for(Project p: projectRepository.getProjectsList()){
            for (Task t: p.getTasks()) {
                if ( !dependsOn.contains(t) && (t.getStatus().equals("AVAILABLE") || t.getStatus().equals("UNAVAILABLE"))){
                    availableTasks.add(t.getId());
                }
            }
        }
        return availableTasks;
    }

    public Collection<Project> getProjectsList() {
        return projectRepository.getProjectsList();
    }

    /**
     * Delete a task along with the tasks that depend on it
     * @param taskId the id of the task we want to delete
     */
    public void deleteTask(long taskId) {
        List<Task> tasks = taskRepository.getTasks();
        for(Task t :tasks){
            for(Task d : t.getDependsOn()){
                if(d.getId() == taskId){
                    taskRepository.deleteTask(t.getId());
                    break;
                }
            }
        }
        taskRepository.deleteTask(taskId);
        actionRepository.clearList();
    }
    /**
     * Returns every single existing task in existence inside the task repository (accessed via the project repository)
     * @return an arraylist of every single existing task inside the task repository
     */
    public List<Task> getAllTasks(){
        Collection<Project> projects =  getProjectsList();
        List<Task> tasks = new ArrayList<>();
        for(Project p : projects){
            tasks.addAll(p.getTasks());
        }
        return tasks;
    }
}
