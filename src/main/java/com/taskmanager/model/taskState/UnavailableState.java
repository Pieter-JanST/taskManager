package com.taskmanager.model.taskState;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Unavailable state of a task
 */
public class UnavailableState implements TaskState {
    @Override
    /**
     * Try to end a task
     * @throws IllegalStateException when trying to start a task that is unavailable
     */
    public void endTask(Task t,boolean failed) {
        throw new IllegalStateException("Can't end a task when it is Unavailable");
    }
    @Override
    /**
     * Switches the state of a task to available if all dependencies are finished
     * @param task The task to check
     */
    public void canSwitchToAvailable(Task task) {
        for (Task t : task.getDependsOn()) {
            if (!t.getStatus().equals("FINISHED")){
                return;
            }
        }
        task.setState(new AvailableState());
    }
    @Override
    /**
     * Tries to assign a user to a task
     * @throws IllegalStateException when trying to assign a user to a task that is unavailable
     */
    public void assignUserToTask(Task task, User user, String roleId) {
        throw new IllegalStateException("cant assign user to task when Unavailable");
    }
    @Override
    /**
     * Tries to remove a user from a task
     * @throws IllegalStateException when trying to remove a user from a task that is unavailable
     */
    public void removeUserFromTask(Task t, User u) {
        throw new IllegalStateException("cant remove user from task when Unavailable");
    }
    @Override
    /**
     * Tries to replace the dependencies of a task
     * @param task The task to replace the dependencies of
     * @param dependencies The new dependencies
     */
    public void replaceDependencies(Task task, ArrayList<Task> dependencies) {
        task.setDependsOn(dependencies);
        canSwitchToAvailable(task);
    }

    @Override
    /**
     * Replace the values of a task
     * @param task The task to replace the values of
     * @param description The new description
     * @param estimatedDuration The new estimated duration
     * @param acceptableDeviation The new acceptable deviation
     * @param roles The new roles
     * @param time The new time
     * @param dependencies The new dependencies
     */
    public void replaceTask(Task task, String description, int estimatedDuration, float acceptableDeviation, List<String> roles, Timestamp time, ArrayList<Task> dependencies) {
        task.replaceTaskValues(description,estimatedDuration,acceptableDeviation,roles,time,dependencies);
        canSwitchToAvailable(task);
    }
    @Override
    /**
     * Tries to undo the end of a task
     * @throws IllegalStateException when trying to undo the end of a task that is unavailable
     */
    public void undoEndTask(Task t){
        throw new IllegalStateException("cant undo end-task when UNAVAILABLE");
    }
    @Override
    /**
     * Returns the status of the unavailable task
     * @return The status of the unavailable task
     */
    public String toString() {
        return "UNAVAILABLE";
    }
}
