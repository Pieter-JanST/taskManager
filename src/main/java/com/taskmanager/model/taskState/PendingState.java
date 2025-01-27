package com.taskmanager.model.taskState;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Pending state of a task
 */
public class PendingState implements TaskState {
    @Override
    /**
     * Tries to end a task
     * @param t The task to end
     * @param failed Whether the task failed or not
     * @throws IllegalStateException when trying to end a task that is pending
     */
    public void endTask(Task t, boolean failed) {
        throw new IllegalStateException("cant end a task when Pending");
    }
    @Override
    /**
     * Tries to switch the state of a task to available
     * @param t The task to check
     */
    public void canSwitchToAvailable(Task t) {}
    @Override
    /**
     * Removes a user from a task
     * @param t The task to remove the user from
     * @param user The user to remove
     * @throws IllegalArgumentException when trying to remove a user from a task that the user is not working on
     */
    public void removeUserFromTask(Task t, User user) {
        if (!t.getNecessaryRoles().containsValue(user)){throw new IllegalArgumentException("User iss not working on this task");}

        for (String key : t.getNecessaryRoles().keySet()) {
            if (t.getNecessaryRoles().get(key) == user){
                t.getNecessaryRoles().put(key,null);
            }
        }

        // check if there is at least 1 dev still working
        boolean isAllNull = true;
        for (User u :t.getNecessaryRoles().values()) {
            if (u != null) {
                isAllNull = false;
                break;
            }
        }
        if (isAllNull){
            t.setState(new AvailableState());
        }
    }
    @Override
    /**
     * Assigns a user to a task
     * @param task The task to assign the user to
     * @param user The user to assign
     * @param roleId The role of the user
     */
    public void assignUserToTask(Task task, User user, String roleId) {
        task.addUserToTask(user,roleId);
        if (!task.getNecessaryRoles().containsValue(null)){
            task.setState(new ExecutingState());
        }
    }

    @Override
    /**
     * Tries to replace the dependencies of a task
     * @param task The task to replace the dependencies of
     * @param dependencies The new dependencies
     * @throws IllegalStateException when trying to replace the dependencies of a task that is pending
     */
    public void replaceDependencies(Task task, ArrayList<Task> dependencies) {
        throw new IllegalStateException("cant update dependencies when Pending");
    }
    @Override
    /**
     * Tries to replace the task
     * @param task The task to replace
     * @param description The new description
     * @param estimatedDuration The new estimated duration
     * @param acceptableDeviation The new acceptable deviation
     * @param roles The new roles
     * @param time The new time
     * @param dependencies The new dependencies
     * @throws IllegalStateException when trying to replace a task that is pending
     */
    public void replaceTask(Task task, String description, int estimatedDuration, float acceptableDeviation, List<String> roles, Timestamp time, ArrayList<Task> dependencies) {
        throw new IllegalStateException("cant replace task when Pending");
    }
    @Override
    /**
     * Tries to undo the end of a task
     * @param t The task to undo the end of
     * @throws IllegalStateException when trying to undo the end of a task that is pending
     */
    public void undoEndTask(Task t){
        throw new IllegalStateException("cant undo end-task when PENDING");
    }
    @Override
    /**
     * Gives the state as a string
     * @return The state as a string
     */
    public String toString() {
        return "PENDING";
    }


}

