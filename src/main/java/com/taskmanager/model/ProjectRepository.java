package com.taskmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that represents a repository for all the projects
 */
public class ProjectRepository {
    /**
     * A repository for all the projects in the form of a map
     * Each project is mapped with a unique identifier
     */
    private HashMap<Long, Project> projectRepository = new HashMap<>();


    /**
     * Adds a project to the global repository
     * @param project The project to be added into the repository
     * @return The project's unique id
     */
    public long addProject(Project project){
        projectRepository.put(project.getId(),project);
        return project.getId();
    }

    /**
     * Extracts clone of a project from the repository via it's unique id
     * @param id The id of the project to be extracted from the repository
     * @return A clone of the project with the given id, or null if no such project exists
     */
    public Project getProjectsById(long id){
        try {
            return projectRepository.get(id).clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns a list of all existing projects inside the global repository
     * @return The collection of all projects
     */
    public ArrayList<Project> getProjectsList(){
        return new ArrayList(projectRepository.values());
    }

    /**
     * Replaces the current repository with a deep clone of the given one
     * @param projectRepository the repository to be cloned and used as a replacement
     */
    public void replaceRepo(HashMap<Long, Project> projectRepository) {
        if (projectRepository == null){throw new IllegalArgumentException("projectRepository cant be null");}
        this.projectRepository = deepCloneMap(projectRepository);
    }

    /**
     * Deep clones a given map
     * @param map The map to be cloned
     * @return Deep clone of the given map
     */
    private HashMap<Long, Project> deepCloneMap(HashMap<Long, Project> map){
        Map<Long, Project> mapCopy = map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> {
                    try {
                        return e.getValue().clone();
                    } catch (CloneNotSupportedException ex) {
                        throw new RuntimeException(ex);
                    }
                }));
        return (HashMap<Long, Project>) mapCopy;
    }

    /**
     * Returns a deep clone of the repository
     * @return A deep clone of the repository
     */
    public HashMap<Long, Project> getProjectAsMap() {
        // Deep clone map
        return deepCloneMap(projectRepository);
    }

    /**
     * Gets a task from a project given the project's id and the task's id
     * @param projectId The id of the project containing the task
     * @param id The id of the task to be extracted
     * @return The task with the given id, or null if no such task exists
     */
    public Task getTask(long projectId, Long id) {
        return projectRepository.get(projectId).getTaskById(id);
    }

    /**
     * Gets a project given it's id
     * @param projectId The id of the project to be extracted
     * @return The project with the given id, or null if no such project exists
     */
    public Project getProject(long projectId) {
        return projectRepository.get(projectId);
    }

    /**
     * Checks if a project with the given id exists
     * @param projectId The id of the project to be checked
     * @return True if a project with the given id exists, false otherwise
     */
    public boolean exists(Long projectId) {
        return projectRepository.containsKey(projectId);
    }

    /**
     * Deletes a project from the repository
     * @param p The project to be deleted
     */
    public void deleteProject(Project p) {
        projectRepository.remove(p.getId());
    }
}
