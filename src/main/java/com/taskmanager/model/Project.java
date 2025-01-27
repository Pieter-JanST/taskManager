package com.taskmanager.model;

import com.taskmanager.util.IdGeneratorUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class representing a project
 */
public class Project implements clockObserver,Cloneable{
    /**
     * The id of the project
     */
    private Long id;
    /**
     * The name of the project
     */
    private String name;
    /**
     * The description of the project
     */
    private String description;
    /**
     * The original system time of the project
     */
    private Timestamp originalTime;
    /**
     * The creation time of the project
     */
    private Timestamp creationTime;
    /**
     * The system time of the project
     */
    private Timestamp pSystemTime;
    /**
     * The due time of the project
     */
    private Timestamp dueTime;
    /**
     * The tasks of the project as a hashmap
     */
    private HashMap<Long, Task> tasks = new HashMap<>();

    /**
     * The total execution time of the project
     */
    private long totalExecutionTime;

    /**
     * initialize a project
     * @param name the name
     * @param desc the description
     * @param dueTime the time it's due
     */
    public Project(String name, String desc, Timestamp dueTime){
        setId(IdGeneratorUtil.createId());
        setName(name);
        setDescription(desc);
        setPSystemTime(null);
        setDueTime(dueTime);
        Clock.addObserver(this);
        totalExecutionTime = 0;
    }

    /**
     * initialize a project with a system time and original system time
     * @param name the name
     * @param desc  the description
     * @param dueTime the time it's due
     * @param systemTime the system time
     * @param original the original time
     */
    public Project(String name, String desc, Timestamp dueTime, Timestamp systemTime, Timestamp original){
        setId(IdGeneratorUtil.createId());
        setName(name);
        setDescription(desc);
        setPSystemTime(systemTime);
        originalTime = original;
        setDueTime(dueTime);
        creationTime = systemTime;
        Clock.addObserver(this);
        totalExecutionTime = 0;
    }

    /**
     * Gets the system time of the project
     * @return the system time
     */
    public Timestamp getPSystemTime(){
        return pSystemTime;
    }

    /**
     * Gets the original time of the project
     * @return
     */
    public Timestamp getOriginalTime() {
        return originalTime;
    }

    /**
     * Gets the name of the project
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the project
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the description of the project
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the creation time of the project
     * @return the creation time
     */
    public Timestamp getCreationTime() {
        return this.creationTime;
    }

    /**
     * Gets the due time of the project
     * @return the due time
     */
    public Timestamp getDueTime() {
        return new Timestamp(dueTime.getTime()+pSystemTime.getTime());
    }

    /**
     * Gets the status of the project
     * @return the status as a string
     */
    public String getStatus() {
        if (tasks.size() == 0){
            return "UNAVAILABLE";
        }
        List<String> statuses = new LinkedList<>();
        for (Task t :tasks.values()) {
            statuses.add(t.getStatus());
        }
        if (statuses.contains("FAILED")){
            return "FAILED";
        }
        else if (statuses.stream().allMatch(str -> str.equals("FINISHED"))){
            return "FINISHED";
        }
        else if (statuses.contains("AVAILABLE")){
            return "AVAILABLE";
        }
        else if (statuses.contains("UNAVAILABLE")){
            return "UNAVAILABLE";
        }
        return "EXECUTING";
    }
    /**
     * Get a list of tasks for the project
     * @return the list of tasks
     */
    public List<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    /**
     * Get a task that belongs to the project by id
     * @return the found task
     * @throws IllegalArgumentException if the task is not found
     */
    public Task getTaskById(long taskId) {
        if (this.tasks.get(taskId) == null){throw new IllegalArgumentException("task not found");}
        return this.tasks.get(taskId);
    }

    /**
     * Add a task to the project
     * @param t the task to add
     * @throws IllegalArgumentException if the task is null
     */
    protected void addTask(Task t){
        if (t == null){
            throw new IllegalArgumentException("task cant be empty");
        }
        tasks.put(t.getId(),t);
    }

    /**
     * Set the description of the project
     * @param description the description to set
     * @throws IllegalArgumentException if the description is empty
     */
    public void setDescription(String description) {
        if (description == null){throw new IllegalArgumentException("description cant be empty");}
        if (description.trim().isEmpty() ){throw new IllegalArgumentException("description cant be empty");}
        this.description = description;
    }

    /**
     * Set the name of the project
     * @param name the name to set
     * @throws IllegalArgumentException if the name is empty
     */
    public void setName(String name) {
        if (name == null){throw new IllegalArgumentException("name cant be empty");}
        if (name.trim().isEmpty() ){throw new IllegalArgumentException("name cant be empty");}
        this.name = name;
    }

    /**
     * Set the creation time of the project
     * @param creationTime the time to set
     * @throws IllegalArgumentException if the time is null
     * @throws IllegalArgumentException if the time is in the past
     */
    public void setCreationTime(Timestamp creationTime) {
        if (creationTime == null){
            throw new IllegalArgumentException("creationTime cant be empty");
        }
        if (creationTime.before(Timestamp.from(Instant.now().minus(5, ChronoUnit.MINUTES)))){
            throw new IllegalArgumentException("creationTime cant be the past");
        }
        this.creationTime = new Timestamp(creationTime.getTime()- getPSystemTime().getTime());
    }

    /**
     * Set the id of the project
     * @param id the id to set
     * @throws IllegalArgumentException if the id is less than 0
     */
    private void setId(Long id) {
        if (id < 0){ throw new IllegalArgumentException("id cant be less then 0");}
        this.id = id;
    }

    /**
     * Set the time the project is due
     * @param dueTime the time to set
     * @throws IllegalArgumentException if the time is null
     * @throws IllegalArgumentException if the time is in the past
     * @throws IllegalArgumentException if the time is before the creation time

     */
    public void setDueTime(Timestamp dueTime) {
        if (dueTime == null){
            throw new IllegalArgumentException("dueTime cant be empty");
        }
        if (dueTime.before(Timestamp.from(Instant.now()))){
            throw new IllegalArgumentException("dueTime cant be in the past");
        }
        if (this.creationTime != null && dueTime.before(this.creationTime)){
            throw new IllegalArgumentException("dueTime cant be before creationTime");
        }
        //this.dueTime = dueTime;
        this.dueTime = new Timestamp(0);
        this.dueTime.setTime(dueTime.getTime()- getPSystemTime().getTime());

    }

    /**
     * Sets the system time of the project
     * @param t the time to set
     */
    public void setPSystemTime(Timestamp t) {
        pSystemTime = t;
    }

    /**
     * Increase the total execution time of the project
     * @param time the time to add
     */
    public void addToTotalExecutionTime(long time){
        totalExecutionTime += time;
    }

    @Override
    /**
     * Update the project's time fields
     * @param original the original system time
     * @param variation the variation in time
     */
    public void updateTime(Timestamp original, Timestamp variation) {
        Timestamp t = new Timestamp(0);
        t.setTime(original.getTime() + variation.getTime());
        setPSystemTime(t);
        originalTime = original;
    }

    /**
     * Gets the total execution time of the project
     * @return the total execution time
     */
    public long getTotalExecutionTime(){
        return totalExecutionTime;
    }

    /**
     * Get the tasks as a map
     * @return the map of tasks
     */
    public HashMap<Long,Task> getTasksAsMap(){
        return tasks;
    }

    @Override
    /**
     * Clone the project
     * @return the cloned project
     * @throws CloneNotSupportedException if the project cannot be cloned
     */
    public Project clone() throws CloneNotSupportedException {

        Project p = (Project)super.clone();

        // again deep copy tasks here
        Map<Long, Task> mapCopy =  p.getTasksAsMap().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> {
                    try {
                        return e.getValue().clone();
                    } catch (CloneNotSupportedException ex) {
                        throw new RuntimeException(ex);
                    }
                }));
        p.setTaskMap((HashMap<Long, Task>) mapCopy);

        return p;
    }

    /**
     * Set the tasks of the project
     * @param mapCopy the map of tasks
     */
    private void setTaskMap(HashMap<Long, Task> mapCopy) {
        this.tasks = mapCopy;
    }

    /**
     * Remove a task from the project
     * @param task the task to remove
     */
    public void removeTask(Task task) {
        this.tasks.remove(task.getId());
    }
}