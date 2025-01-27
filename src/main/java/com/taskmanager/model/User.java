package com.taskmanager.model;

import com.taskmanager.model.roles.*;
import com.taskmanager.util.IdGeneratorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a user of the TaskManager application
 */
public class User implements Cloneable, TaskObserver {
    /**
     * The unique id of the user
     */
    private final long id ;
    /**
     * The username of the user
     */
    private String username;
    /**
     * The password of the user
     */
    private String password;
    /**
     * The task the user is currently working on
     */
    private Task workingTask = null;
    /**
     * The roles of the user
     */
    private final List<String> roles = new ArrayList<>();


    /**
     * Initialize a User
     * @param user the userName
     * @param password the password
     */
    public User(String user, String password) {
        id = IdGeneratorUtil.createId();
        setUsername(user);
        setPassword(password);
        setRoles(new ArrayList<>());

    }

    /**
     * Initialize a User
     * @param user the userName
     * @param password the password
     * @param roles the roles
     */
    public User(String user, String password, List<String> roles) {
        id = IdGeneratorUtil.createId();
        setUsername(user);
        setPassword(password);
        setRoles(roles);
    }

    /**
     * Gets the id of the user
     * @return the id of the user
     */
    public Long getId() {
            return id;
    }

    /**
     * Gets the username of the user
     * @return the username of the user
     */
    public String getUsername() {
            return this.username;
    }

    /**
     * Gets the password of the user
     * @return the password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the roles of the user
     * @return a copy of the roles of the user
     */
    public List<String> getRoles() {return List.copyOf(roles);}

    /**
     * Gets the working task of the user
     * @return the task that the user is working on
     */
    public Task getWorkingTask() {
        return this.workingTask;
    }

    /**
     * Sets the password of the user
     * @param password the password to be set
     * @throws IllegalArgumentException if the password is empty
     */
    public void setPassword(String password) {
        if (password.trim().isEmpty()){ throw new IllegalArgumentException("Password can't be empty");}
        this.password = password;
    }

    /**
     * Sets the username of the user
     * @param username the username to be set
     * @throws IllegalArgumentException if the username is empty
     */
    public void setUsername(String username) {
        if (username.trim().isEmpty()){ throw new IllegalArgumentException("Username can't be empty");}
        this.username = username;
    }

    /**
     * Sets the roles of the user
     * @param roles the roles to be set
     * @throws IllegalArgumentException if the roles are empty
     */
    private void setRoles(List<String> roles) {
        if (roles == null){ throw new IllegalArgumentException("Role(s) can't be empty");}
        for (String role :roles) {
            addRole(role);
        }
    }

    /**
     * Sets the working task of the user
     * @param t the task to be set
     */
    public void setWorkingTask(Task t) {
        this.workingTask = t;
    }

    /**
     * Removes the working task of the user
     */
    public void removeWorkingTask() {
        this.workingTask = null;
    }

    /**
     * Adds a single role to the user's roles, if it is a valid role. Throws an error otherwise
     * @param role The role to be assigned to the user
     */
    public void addRole(String role) {
        if (role == null){ throw new IllegalArgumentException("Role can't be empty");}
        try{
            AvailableRoles.valueOf(role);
            this.roles.add(role);
        }
        catch (Exception e){
            throw new IllegalArgumentException(role+" is an invalid role");
        }

    }

    /**
     * Makes a clone of the user
     * @return the clone of the user
     * @throws CloneNotSupportedException if the cloning is not supported
     */
    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    /**
     * Checks the availability of the user
     */
    @Override
    public void checkAvailability() {
        if (workingTask.getStatus().equals("FINISHED") || workingTask.getStatus().equals("FAILED")){
            setWorkingTask(null);
        }
    }

    /**
     * Removes the working task of the user
     * @param task the task to be removed
     */
    @Override
    public void removedDependency(Task task) {
        this.workingTask = null;
    }

    /**
     * Updates the working task of the user
     * @param task the task to be updated
     */
    @Override
    public void updateDependentTask(Task task) {
        this.workingTask = task;
    }
}
