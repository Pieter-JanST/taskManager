package com.taskmanager.model;

/**
 * An interface for classes that want to observe a {@link Task}.
 */
public interface TaskObserver {
    /**
     * Checks if the task is available
     */
    void checkAvailability();

    /**
     * Removes a dependency from the task
     * @param task The task that has a dependency removed
     */
    void removedDependency(Task task);

    /**
     * Updates the dependent task
     * @param task The task that has a dependency updated
     */
    void updateDependentTask(Task task);
}
