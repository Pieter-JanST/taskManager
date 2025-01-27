package com.taskmanager.model.taskState;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

/**
 * An interface for the states of a task
 */
public interface TaskState {
    /**
     * Ends a task
     * @param t The task to end
     * @param failed Whether the task failed or not
     */
    void endTask(Task t, boolean failed);

    /**
     * Switches the state of a task to available if all dependencies are finished
     * @param t The task to check
     */
    void canSwitchToAvailable(Task t);

    /**
     * Removes a user from a task
     * @param t The task to remove the user from
     * @param u The user to remove
     */
    void removeUserFromTask(Task t,User u);

    /**
     * Assigns a user to a task
     * @param task The task to assign the user to
     * @param user The user to assign
     * @param roleId The role of the user
     */
    void assignUserToTask(Task task, User user, String roleId);

    /**
     * Replaces the dependencies of a task
     * @param task The task to replace the dependencies of
     * @param dependencies The new dependencies
     */
    void replaceDependencies(Task task, ArrayList<Task> dependencies);

    /**
     * Replace the task's values
     * @param task The task to replace
     * @param description The new description
     * @param estimatedDuration The new estimated duration
     * @param acceptableDeviation The new acceptable deviation
     * @param roles The new roles
     * @param time The new time
     * @param dependencies The new dependencies
     */
    void replaceTask(Task task, String description, int estimatedDuration, float acceptableDeviation, List<String> roles, Timestamp time, ArrayList<Task> dependencies);

    /**
     * Undoes the ending of a task
     * @param task The task to start
     */
    void undoEndTask(Task task);

}

