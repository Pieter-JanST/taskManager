package com.taskmanager.model.taskState;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

/**
 * A class representing the Available state of a task
 */
public class AvailableState implements TaskState {
    @Override
    /**
     * Tries to end a task
     * @param t The task to end
     * @param failed Whether the task failed or not
     * @throws IllegalStateException when trying to end a task that is available
     */
    public void endTask(Task t,boolean failed) {
        throw new IllegalStateException("cant end a task when Available");
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
        throw new IllegalStateException("cant remove user from task when Available");
    }
    @Override
    /**
     * Assigns a user to a task
     * @param task The task to assign the user to
     * @param user The user to assign to the task
     * @param roleId The role of the user
     */
    public void assignUserToTask(Task task, User user, String roleId) {
        task.addUserToTask(user,roleId);
        task.getTimeSpan().startTime();
        task.setState(new PendingState());
        if (!task.getNecessaryRoles().containsValue(null)){
            task.setState(new ExecutingState());
        }
    }

    @Override
    /**
     * Replaces the dependencies of a task
     * @param task The task to replace the dependencies of
     * @param dependencies The new dependencies
     */
    public void replaceDependencies(Task task, ArrayList<Task> dependencies) {
        task.setDependsOn(dependencies);
        for (Task t: dependencies) {
            if (!t.getStatus().equals("Finished")){
                task.setState(new UnavailableState());
                break;
            }
        }
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
     * Tries to undo the ending of a task
     * @param t The task to undo the ending of
     * @throws IllegalStateException when trying to undo the ending of a task that is available
     */
    public void undoEndTask(Task t){
        throw new IllegalStateException("cant undo end-task when AVAILABLE");
    }

    @Override
    /**
     * Returns the status of a task
     * @return The status of a task
     */
    public String toString() {
        return "AVAILABLE";
    }
}
