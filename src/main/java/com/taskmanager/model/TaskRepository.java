package com.taskmanager.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that represents a repository for all the tasks
 */
public class TaskRepository {

    /**
     * A repository for all the tasks
     * Each task is mapped with a unique identifier
     */
    private HashMap<Long, Task> taskRepository = new HashMap<>();


    /**
     * Extracts a list of every existing task
     * @return A list of all existing tasks inside the repository
     */
    public List<Task> getTasks() {
        return new ArrayList(taskRepository.values());
    }

    /**
     * Extracts a clone of the task with id taskId from the repository
     * @param taskId The unique identifier of the task to be extracted from the repository
     * @return A clone of the task with unique id taskId, or null if no mapping to such exists
     */
    public Task getTaskById(long taskId) {
        try{
            return taskRepository.get(taskId).clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Used to store a task in the repository
     * @param task   The task to be saved
     */
    public void addTask(Task task) {
        taskRepository.put(task.getId(), task);
    }

    /**
     * check if the task is stored in the repository
     * @param taskId The unique identifier of the task to be saved
     */
    public boolean exists(Long taskId) {
        return taskRepository.containsKey(taskId);
    }

    /**
     * Makes a deep clone of the given map
     * @param map the map to be cloned
     * @return A deep clone of the given map
     */
    private HashMap<Long, Task> deepCloneMap(HashMap<Long, Task> map){
        Map<Long, Task> mapCopy = map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> {
                    try {
                        return e.getValue().clone();
                    } catch (CloneNotSupportedException ex) {
                        throw new RuntimeException(ex);
                    }
                }));
        return (HashMap<Long, Task>) mapCopy;
    }

    /**
     * Replaces the current repository with a deep clone of the given one
     * @param taskRepository the repository to be cloned and used as a replacement
     */
    public void replaceRepo(HashMap<Long, Task> taskRepository) {
        if (taskRepository == null){throw new IllegalArgumentException("projectRepository cant be null");}
        this.taskRepository = deepCloneMap(taskRepository);

    }

    /**
     * Gets a deep clone of the repository
     * @return A deep clone of the repository
     */
    public HashMap<Long, Task> getProjectAsMap() {
        return deepCloneMap(taskRepository);
    }

    /**
     * Deletes a task from the repository
     * @param taskId The unique identifier of the task to be deleted
     */
    public void deleteTask(long taskId) {
        if (!taskRepository.containsKey(taskId)){
            throw new IllegalArgumentException("taskRepository does not contain taskId " +taskId);
        }
        taskRepository.get(taskId).prepareDelete();
        taskRepository.remove(taskId);
    }

    /**
     * Ends a task
     * @param taskId The unique identifier of the task to be ended
     * @param failed Whether the task was ended because it failed or finished successfully
     */
    public void endTask(long taskId, boolean failed) {
        if (!taskRepository.containsKey(taskId)){
            throw new IllegalArgumentException("taskRepository does not contain taskId " +taskId);
        }
        taskRepository.get(taskId).endTask(failed);
    }

    /**
     * Assigns a user to a task
     * @param taskId The unique identifier of the task to which the user will be assigned
     * @param user The user to be assigned to the task
     * @param roleId The role of the user in the task
     */
    public void assignUserToTask(long taskId, User user, String roleId) {
        if (!taskRepository.containsKey(taskId)){
            throw new IllegalArgumentException("taskRepository does not contain taskId " +taskId);
        }
        taskRepository.get(taskId).assignUserToTask(user, roleId);
    }

    /**
     * Updates the dependencies of a task
     * @param taskId The unique identifier of the task whose dependencies will be updated
     * @param newDependenciesId The new dependencies of the task
     */
    public void updateTaskDependencies(long taskId, List<Long> newDependenciesId) {
        Task task = taskRepository.get(taskId);
        ArrayList<Task> newDependencies = new ArrayList<>();

        for (Long d : newDependenciesId) {
            if (!this.exists(d)) {
                throw new IllegalArgumentException("Task " + d + " does not exist in the repository");
            }
            newDependencies.add(taskRepository.get(d));
        }
        task.replaceDependencies(newDependencies);
    }

    /**
     * Undoes the ending of a task
     * @param taskId The unique identifier of the task whose end will be undone
     */
    public void undoEndTask(long taskId) {
        taskRepository.get(taskId).undoEndTask();
    }
}

