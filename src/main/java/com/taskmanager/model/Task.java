package com.taskmanager.model;

import com.taskmanager.model.taskState.*;
import com.taskmanager.util.IdGeneratorUtil;
import com.taskmanager.model.roles.*;


import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A task is a unit of work that has to be done in a project.
 */
public class Task implements TaskObserver, Cloneable {
    /**
     * The unique id of the task
     */
    private Long id;
    /**
     * The description of the task
     */
    private String description;
    /**
     * The estimated duration of the task in minutes
     */
    private int estimatedDuration;
    /**
     * The acceptable deviation of the task expressed as a percentage
     */
    private float acceptableDeviation;
    /**
     * The roles that are necessary to complete the task
     */
    protected HashMap<String,User> necessaryRoles = new HashMap<>();
    /**
     * The state of the task
     */
    private TaskState state = new UnavailableState();
    /**
     * The time span of the task
     */
    private TimeSpan timeSpan;
    /**
     * List of tasks that this task depends on
     */
    private ArrayList<Task> dependencies = new ArrayList<>();
    /**
     * The project that the task belongs to
     */
    private Project project;
    /**
     * List of taskObservers that observe this task
     */
    private final List<TaskObserver> taskObservers = new ArrayList<>();

    /**
     * Constructor for a task without a dependsOn and a system time
     * @param description The description of the task
     * @param estimatedDuration The estimated duration of the task in minutes
     * @param acceptableDeviation The acceptable deviation of the task expressed as a percentage
     * @param project The project that the task belongs to
     * @param roles The roles that are necessary to complete the task
     */
    public Task(String description, int estimatedDuration, float acceptableDeviation, Project project, List<String> roles){
        setId(IdGeneratorUtil.createId());
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setNecessaryRoles(roles);
        setProject(project);
        this.state.canSwitchToAvailable(this);
    }

    /**
     * Constructor for a task with a dependsOn and a system time
     * @param description The description of the task
     * @param estimatedDuration The estimated duration of the task in minutes
     * @param acceptableDeviation The acceptable deviation of the task expressed as a percentage
     * @param project The project that the task belongs to
     * @param tsSystemTime The system time of the task
     * @param roles The roles that are necessary to complete the task
     * @param dependsOns The tasks that this task depends on
     */
    public Task(String description, int estimatedDuration, float acceptableDeviation, Project project, Timestamp tsSystemTime, List<String> roles, ArrayList<Task> dependsOns){
        setId(IdGeneratorUtil.createId());
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setNecessaryRoles(roles);
        setTimeSpan(tsSystemTime);
        setProject(project);
        setDependsOn(dependsOns);
        this.state.canSwitchToAvailable(this);
    }

    /**
     * Constructor for a task with a dependsOn but without a system time
     * @param description The description of the task
     * @param estimatedDuration The estimated duration of the task in minutes
     * @param acceptableDeviation The acceptable deviation of the task expressed as a percentage
     * @param project The project that the task belongs to
     * @param roles The roles that are necessary to complete the task
     * @param dependsOns The tasks that this task depends on
     */
    public Task(String description, int estimatedDuration, float acceptableDeviation, Project project,List<String> roles, ArrayList<Task> dependsOns){
        setId(IdGeneratorUtil.createId());
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setNecessaryRoles(roles);
        setProject(project);
        setDependsOn(dependsOns);
        this.state.canSwitchToAvailable(this);
    }

    /**
     * Gets the id of the task.
     * @return the id of the task
     */
    public Long getId() {return id;}

    /**
     * Gets the description of the task.
     * @return the description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the estimated duration of the task.
     * @return the estimated duration of the task
     */
    public int getEstimatedDuration(){
        return estimatedDuration;
    }

    /**
     * Gets the acceptable deviation of the task.
     * @return the acceptable deviation of the task as a float
     */
    public float getAcceptableDeviation(){
        return acceptableDeviation;
    }

    /**
     * Gets the necessary roles of the task.
     * @return a map of the necessary roles of the task
     */
    public Map<String,User> getNecessaryRoles() {
        return this.necessaryRoles;
    }

    /**
     * Gets the status of the task.
     * @return the status of the task as a string
     */
    public String getStatus() {
        return state.toString();}

    /**
     * Gets the project that the task belongs to.
     * @return the project that the task belongs to
     */
    public Project getProject() {
        return project;
    }

    /**
     * Gets the dependencies of the task.
     * @return A new list of the dependencies of the task
     */
    public ArrayList<Task> getDependsOn(){return (ArrayList<Task>) dependencies.clone();}

    /**
     * Gets the time span of the task.
     * @return the time span of the task
     */
    public TimeSpan getTimeSpan() {return timeSpan;}

    /**
     * Sets the time span of the task.
     * @param tsSystemTime the time span of the task
     * @throws IllegalArgumentException if the time span is null
     */
    private void setTimeSpan(Timestamp tsSystemTime){
        if (tsSystemTime == null){ throw new IllegalArgumentException("tsSystemTime cant be empty");}
        this.timeSpan = new TimeSpan(tsSystemTime);
    }

    /**
     * Sets the project of the task.
     * @param project the project of the task
     * @throws IllegalArgumentException if the project is null
     */
    private void setProject(Project project) {
        if (project == null){ throw new IllegalArgumentException("project cant be empty");}
        project.addTask(this);
        this.project = project;
    }

    /**
     * Sets the state of the task.
     * @param state the state of the task
     * @throws IllegalArgumentException if the state is null
     */
    public void setState(TaskState state) {
        if (state == null){ throw new IllegalArgumentException("status cant be empty");}
        this.state = state;}

    /**
     * Sets the description of the task.
     * @param description the description of the task
     * @throws IllegalArgumentException if the description is empty
     */
    public void setDescription(String description){
        if (description == null){throw new IllegalArgumentException("description cant be empty");}
        if (description.trim().isEmpty()){ throw new IllegalArgumentException("description cant be empty");}
        this.description = description;
    }

    /**
     * Sets estimated duration of the task.
     * @param estimatedDuration the estimated duration of the task
     * @throws IllegalArgumentException if the estimatedDuration is smaller or equal to 0
     */
    public void setEstimatedDuration(int estimatedDuration){
        if (estimatedDuration <= 0){ throw new IllegalArgumentException("estimatedDuration needs to be greater then 0");}
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * Sets the acceptable deviation of the task.
     * @param acceptableDeviation the acceptable deviation of the task
     * @throws IllegalArgumentException if the acceptableDeviation is smaller or equal to 0
     */
    public void setAcceptableDeviation(float acceptableDeviation){
        if (acceptableDeviation <= 0){ throw new IllegalArgumentException("acceptableDeviation needs to be greater then 0");}
        this.acceptableDeviation = acceptableDeviation;
    }

    /**
     * Sets the necessary roles for the task.
     * @param roles the roles that are necessary for the task
     * @throws IllegalArgumentException if the roles are null
     * @throws IllegalArgumentException if the roles are not available roles
     */
    public void setNecessaryRoles(List<String> roles){
        if (roles == null){throw new IllegalArgumentException("Role cannot be null");}

        for (String r : roles) {
            try {
                AvailableRoles.valueOf(r);
            }catch (Exception e){
                throw new IllegalArgumentException("roles can only contain availableRoles");
            }
        }
        for (String role: roles) {
            this.necessaryRoles.put(role+this.necessaryRoles.size(),null);
        }
    }

    /**
     * Replace the current dependencies by the new given ones.
     * @param dependencies the new dependencies
     */
    public void replaceDependencies(ArrayList<Task> dependencies){
        this.state.replaceDependencies(this,dependencies);
        //update the clone stored in the dependencies List of the observers
        for(TaskObserver to: this.taskObservers){
            to.updateDependentTask(this);
        }
    }


    /**
     * Replace the current values of the task by the new given ones.
     * @param description the new description
     * @param estimatedDuration the new estimated duration
     * @param acceptableDeviation the new acceptable deviation
     * @param roles the new roles
     * @param time the new time span
     * @param dependencies the new dependencies
     */
    public void replaceTask(String description, int estimatedDuration, float acceptableDeviation, List<String> roles,Timestamp time, ArrayList<Task> dependencies){
        this.state.replaceTask(this,description,estimatedDuration,acceptableDeviation,roles, time, dependencies);
    }

    /**
     * Replace the current values of the task by the new given ones.
     * @param description the new description
     * @param estimatedDuration the new estimated duration
     * @param acceptableDeviation the new acceptable deviation
     * @param roles the new roles
     * @param tsSystemTime the new time span
     * @param dependencies the new dependencies
     * @throws Exception if the new values are not valid
     */
    public void replaceTaskValues(String description, int estimatedDuration, float acceptableDeviation, List<String> roles, Timestamp tsSystemTime, ArrayList<Task> dependencies){
        String tempDescription = this.getDescription();
        int tempEstimatedDuration = this.getEstimatedDuration();
        float tempAcceptableDeviation = this.getAcceptableDeviation();
        ArrayList<Task> tempDependsOn = this.getDependsOn();
        this.necessaryRoles = new HashMap<>();
        try {
            this.setDescription(description);
            this.setEstimatedDuration(estimatedDuration);
            this.setAcceptableDeviation(acceptableDeviation);
            this.setDependsOn(dependencies);
            this.setNecessaryRoles(roles);
            this.timeSpan = new TimeSpan(tsSystemTime);
        }
        catch (Exception e){
            this.setDescription(tempDescription);
            this.setEstimatedDuration(tempEstimatedDuration);
            this.setAcceptableDeviation(tempAcceptableDeviation);
            this.setDependsOn(tempDependsOn);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Set the list of dependant tasks
     * @param dependencies the list
     * @throws IllegalArgumentException if the list is null
     * @throws IllegalArgumentException if the task depends on itself
     * @throws IllegalArgumentException if a task from the dependencies list is not available
     * @throws IllegalArgumentException if there is a loop in the dependencies
     */
    public void setDependsOn(ArrayList<Task> dependencies){
        if (dependencies == null) { throw new IllegalArgumentException("dependencies cant be null");}
        for (Task t: dependencies) {
            if (t == this){
                throw new IllegalArgumentException("Task cant depend on itself");

            }
            if (! (t.getStatus().equals("AVAILABLE") || t.getStatus().equals("UNAVAILABLE")) ) {
                throw new IllegalArgumentException("Task " + t.getId() + " is not available");
            }
        }
        ArrayList<Task> oldDependencies = this.getDependsOn();
        this.dependencies = dependencies;
        /* replace the dependencies to test for loops */

        ArrayList<Long> visited = new ArrayList<>();
        ArrayList<Long> recStack = new ArrayList<>();
        if(checkForLoops(this, visited, recStack)){
            this.dependencies = oldDependencies;
            throw new IllegalArgumentException("Loop in the dependencies");
        }
        for (Task t : this.dependencies) {
            t.addObserver(this);
        }
    }

    /**
     * Finds loops in newly added dependencies
     * @param startTask The task of which new dependencies will be added & the reason why a check is to be done
     * @param visited The visited tasks in the dependency graph yet
     * @return true/false if loops exist
     */
    public boolean checkForLoops(Task startTask, ArrayList<Long> visited, ArrayList<Long> recursiveStack){

        /* https://www.geeksforgeeks.org/detect-cycle-in-a-graph/https://www.geeksforgeeks.org/detect-cycle-in-a-graph/ */
        if (recursiveStack.contains(startTask.getId())){
            return true;
            /* we've re-arrived in an already visited node */
        }
        if(visited.contains(startTask.getId())){
            return false;
            /* no need to revisit nodes we already processed */
        }

        visited.add(startTask.getId());
        recursiveStack.add(startTask.getId());

        for(Task t: startTask.getDependsOn()) {
            if(checkForLoops(t, visited, recursiveStack)) {
                return true;
            }
        }
        recursiveStack.remove(startTask.getId());
        return false;
    }


    /**
     * Sets the id of the task
     * @param l the id
     * @throws IllegalArgumentException if the id is smaller then 0
     */
    private void setId(long l) {
        if (l < 0){ throw new IllegalArgumentException("id needs to be greater or equal 0");}
        this.id=l;}

    /**
     * Assign a user to a task
     * @param u the user to be assigned
     * @param roleId the role the user will be assigned to
     */
    public void assignUserToTask(User u,String roleId){
        this.state.assignUserToTask(this,u,roleId);
    }


    /**
     * add a user to the task
     * @param u the user to be assigned
     * @param roleId the role the user will be assigned to
     * @throws IllegalArgumentException if the user already works on the task as another role
     * @throws IllegalArgumentException if the roleId does not exist
     * @throws IllegalArgumentException if the user does not have the correct role
     */
    public void addUserToTask(User u,String roleId){
        if (necessaryRoles.containsValue(u)){
            throw new IllegalArgumentException("user already works on task as other role");
        }
        if (!necessaryRoles.containsKey(roleId)){
            throw new IllegalArgumentException("roleId does not exists");
        }
        if (u.getRoles().contains(roleId.substring(0, roleId.length() - 1))){
            if (u.getWorkingTask() != null){
                u.getWorkingTask().removeUserFromTask(u);
            }
            u.setWorkingTask(this);
            necessaryRoles.put(roleId,u);
            taskObservers.add(u);
        }
        else {
            throw new IllegalArgumentException("User doesnt have the correct role");
        }
    }
    /**
     * remove the user from working on this task
     * @param u the user to be unassigned
     */
    public void removeUserFromTask(User u) {
       this.state.removeUserFromTask(this,u);
    }

    /**
     * End a task and add the execution time to the project.
     * @param failed true if the task failed
     */
    public void endTask(boolean failed){
        state.endTask(this,failed);
        long minutes = this.timeSpan.getEndTime().getTime() - this.timeSpan.getStartTime().getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(minutes);
        project.addToTotalExecutionTime(diffInMinutes);
    }

    /**
     * gets executed when a dependant task his state has been updated to finished
     * if all the dependant tasks are finished the state shall switch to Available
     */
    @Override
    public void checkAvailability()
    {
        this.state.canSwitchToAvailable(this);
    }

    /**
     * adds an observer to the list of observers
     * @param taskObserver the observer to be added
     */
    private void addObserver(TaskObserver taskObserver) {
        if (taskObserver == null){throw new IllegalArgumentException("taskObserver cant be null");}
        this.taskObservers.add(taskObserver);
    }

    /**
     * removes an observer from the list of observers
     * @param taskObserver the observer to be removed
     */
    private void removeObserver(TaskObserver taskObserver) {
        if (taskObserver == null){throw new IllegalArgumentException("taskObserver cant be null");}
        this.taskObservers.remove(taskObserver);
    }

    /**
     * Notifies all observers that the dependent task has been updated
     */
    public void notifyObservers() {
        for (TaskObserver observer : taskObservers) {
            observer.updateDependentTask(this);
            observer.checkAvailability();
        }
    }

    /**
     * removes all dependencies
     */
    public void clearDependsOn(){
        replaceDependencies(new ArrayList<>());
    }

    /**
     * Checks the time span of a task
     * @return "Task was finished with a delay" if the task was finished with a delay
     * @return "Task was finished early" if the task was finished early
     * @return "Task is finished on time" if the task was finished on time
     * @return "Task is not finished yet" if the task is not finished yet
     */
    public String timeCheck(){
        if(state.toString().equals("FINISHED")){
            long max = getTimeSpan().getStartTime().getTime()/1000 + (1 + (long) getAcceptableDeviation()/100) * (long) getEstimatedDuration()*60; //startTime in seconds like added time
            long min = getTimeSpan().getStartTime().getTime()/1000 + (1 - (long) getAcceptableDeviation()/100) * (long) getEstimatedDuration()*60; //idem

            if (getTimeSpan().getEndTime().getTime()/1000 > max){
                return "Task was finished with a delay";
            }
            else if (getTimeSpan().getEndTime().getTime()/1000 < min){
                return "Task was finished early";
            }
            else {
                return "Task is finished on time";
            }
        }
        return "Task is not finished yet";
    }

    /**
     * Makes a clone of the task
     * @return the clone of the task
     * @throws CloneNotSupportedException if the task can't be cloned
     */
    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }

    /**
     * Replace the dependencies List with a new one
     * @param mapCopy the new dependencies List
     */
    private void replaceNecessaryRoles(HashMap<String, User> mapCopy) {
        this.necessaryRoles = mapCopy;

    }

    /**
     * For taskObservers: remove a dependency from the dependencies List
     * @param task the task that has been removed
     */
    @Override
    public void removedDependency(Task task) {
        this.dependencies.remove(task);
        this.checkAvailability();
        this.removeObserver(task);
    }

    /**
     * Prepare the task to be deleted
     */
    public void prepareDelete() {
        this.project.removeTask(this);
        for (TaskObserver observer : taskObservers) {
            observer.removedDependency(this);
        }
    }

    /**
     * For taskObservers: if a Task updates it dependencies, update the copy of it stored in
     * the dependencies List.
     * @param task the task that has been updated
     */
    @Override
    public void updateDependentTask(Task task){
        Task search = null;
        for(Task t: dependencies){
            if (t.getId().equals(task.getId())){
                search = t;
            }
        }
        if (search != null){
            this.dependencies.remove(search);
        }
        this.dependencies.add(task);
    }

    /**
     * Undo the end of this task
     */
    public void undoEndTask() {
        this.state.undoEndTask(this);
    }

    /**
     * Force remove a user from a task or change the state to available if all users are removed
     * @param user the user to be removed
     */
    public void forceRemoveUserFromTask(User user) {
        for (String key : this.getNecessaryRoles().keySet()) {
            if (this.getNecessaryRoles().get(key) == user){
                this.getNecessaryRoles().put(key,null);
                break;
            }
        }
        
        boolean isAllNull = true;
        for (User u :this.getNecessaryRoles().values()) {
            if (u != null) {
                isAllNull = false;
                break;
            }
        }
        if (isAllNull){
            this.setState(new AvailableState());
        }
        else {
            this.setState(new PendingState());
        }
    }
}
