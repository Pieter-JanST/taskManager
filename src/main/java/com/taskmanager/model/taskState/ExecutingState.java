package com.taskmanager.model.taskState;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Executing state of a task
 */
public class ExecutingState implements TaskState {
    @Override
    /**
     * End a task
     * @param t The task to end
     * @param failed Whether the task failed or not
     */
    public void endTask(Task t, boolean failed) {
        t.getTimeSpan().endTime();
        if (failed){
            t.setState(new FailedState());
            t.notifyObservers();
        }
        else {
            t.setState(new FinishedState());
            t.notifyObservers();
        }
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
    public void removeUserFromTask(Task t, User user) {
        throw new IllegalStateException("cant unAssign user from task when Executing");
    }
    @Override
    /**
     * Tries to assign a user to a task
     * @param task The task to assign the user to
     * @param user The user to assign to the task
     * @param roleId The role of the user
     * @throws IllegalStateException when trying to assign a user to a task that is finished
     */
    public void assignUserToTask(Task task, User user, String roleId) {
        throw new IllegalStateException("cant assign user to task when Executing");
    }
    @Override
    /**
     * Tries to replace the dependencies of a task
     * @param task The task to replace the dependencies of
     * @param dependencies The new dependencies
     * @throws IllegalStateException when trying to replace the dependencies of a task that is finished
     */
    public void replaceDependencies(Task task, ArrayList<Task> dependencies) {
        throw new IllegalStateException("cant update dependencies when Executing");
    }
    @Override
    /**
     * Tries to replace the description of a task
     * @param task The task to replace the description of
     * @param description The new description
     * @param estimatedDuration The new estimated duration
     * @param acceptableDeviation The new acceptable deviation
     * @param roles The new roles
     * @param time The new time
     * @param dependencies The new dependencies
     * @throws IllegalStateException when trying to replace the description of a task that is finished
     */
    public void replaceTask(Task task, String description, int estimatedDuration, float acceptableDeviation, List<String> roles, Timestamp time, ArrayList<Task> dependencies) {
        throw new IllegalStateException("cant replace task when Executing");
    }
    @Override
    /**
     * Undoes the ending of a task
     * @param t The task to undo the ending of
     * @throws IllegalStateException when trying to undo the ending of a task that is finished
     */
    public void undoEndTask(Task t){
        throw new IllegalStateException("cant undo end-task when EXECUTING");
    }
    @Override
    /**
     * Returns the state as a string
     * @return The state as a string
     */
    public String toString() {
        return "EXECUTING";
    }

}
