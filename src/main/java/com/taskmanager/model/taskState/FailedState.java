package com.taskmanager.model.taskState;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Failed state of a task
 */
public class FailedState implements TaskState {
    @Override
    /**
     * Try to end a task
     * @throws IllegalStateException when trying to start a task that is failed
     */
    public void endTask(Task t,boolean failed) {
        throw new IllegalStateException("cant end a task when Failed");
    }
    @Override
    /**
     * Tries to switch the state of a task to available
     * @param t The task to check
     */
    public void canSwitchToAvailable(Task t) {}

    @Override
    /**
     * Tries to switch the state of a task to unavailable
     * @param t The task to check
     * @param u The user to remove from the task
     * @throws IllegalStateException when trying to switch the state of a task to unavailable
     */
    public void removeUserFromTask(Task t, User u) {
        throw new IllegalStateException("cant remove user from task when Failed");
    }
    @Override
    /**
     * Tries to assign a user to a task
     * @param task The task to assign the user to
     * @param user The user to assign to the task
     * @param roleId The role of the user
     * @throws IllegalStateException when trying to assign a user to a task that is failed
     */
    public void assignUserToTask(Task task, User user, String roleId) {
        throw new IllegalStateException("cant assign user to task when Failed");
    }
    @Override
    /**
     * Tries to replace the dependencies of a task
     * @param task The task to replace the dependencies of
     * @param dependencies The new dependencies
     * @throws IllegalStateException when trying to replace the dependencies of a task that is failed
     */
    public void replaceDependencies(Task task, ArrayList<Task> dependencies) {
        throw new IllegalStateException("cant update dependencies when Failed");
    }
    @Override
    /**
     * Replaces the values of a task
     * @param task The task to replace the values of
     * @param description The new description
     * @param estimatedDuration The new estimated duration
     * @param acceptableDeviation The new acceptable deviation
     * @param roles The new roles
     * @param time The new time
     * @param dependencies The new dependencies
     */
    public void replaceTask(Task task, String description, int estimatedDuration, float acceptableDeviation, List<String> roles, Timestamp time, ArrayList<Task> dependencies) {
        task.replaceTaskValues(description,estimatedDuration,acceptableDeviation,roles, time, dependencies);
        task.setState(new UnavailableState());
        task.checkAvailability();
    }
    @Override
    /**
     * TUndoes the end of a task
     * @param t The task to undo the end of
     */
    public void undoEndTask(Task t){
        t.setState(new ExecutingState());
    }
    @Override
    /**
     * Returns the state as a string
     * @return The state as a string
     */
    public String toString() {
        return "FAILED";
    }

}
