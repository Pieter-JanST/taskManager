package com.taskmanager.model;

import com.taskmanager.controller.UserService;

import java.util.*;

public class ActionRepository {
    private final LinkedList<Action> actionRepository = new LinkedList<>();
    int pointer  = -1;


    /**
     * Adds an action to the global repository
     * @param action The action to be added into the repository
     */
    public void addAction(Action action){
        if (action ==null){
            throw new IllegalArgumentException("action cant be null");
        }
        while (pointer < actionRepository.size()-1){
            actionRepository.removeLast();
        }
        pointer+=1;
        actionRepository.add(action);
    }


    /**
     * Extracts the last added action from the repository
     * @return The action with the given id, or null if no such project exists
     */
    public Action getLastAction(){
        if (pointer <0){
            return null;
        }
        return actionRepository.get(pointer);
    }
    public void undoAction(User user,ProjectRepository projectRepository,TaskRepository taskRepository,UserService userService){
        if (pointer >= 0){
            if (user.getRoles().contains("ProjectManager") || user.getId() == actionRepository.get(pointer).getUserId()) {
                actionRepository.get(pointer).undo();
                pointer -= 1;
            }
            else {
                throw new IllegalArgumentException("no access to undo last action");
            }
        }
        else {
            throw new IllegalArgumentException("no action to undo");
        }

    }
    public void redoAction(User user, ProjectRepository projectRepository, TaskRepository taskRepository, UserService userService){
        if (pointer < actionRepository.size()-1){
            if (user.getRoles().contains("ProjectManager") || user.getId() == actionRepository.get(pointer+1).getUserId())
            {
                pointer+=1;
                actionRepository.get(pointer).redo();
            }
            else {
                throw new IllegalArgumentException("no access to redo last action");
            }

        }
        else {
            throw new IllegalArgumentException("no action to redo");
        }
    }
    public void clearList() {
        pointer = -1;
        actionRepository.clear();
    }
    /**
     * Returns a list of all existing actions inside the global repository
     * @return The collection of all actions
     */
    public List<Action> getActionList(User u){
        return actionRepository;
    }

    public List<Action> getActionUndoList(User u){
        if (u.getRoles().contains("ProjectManager")){
            LinkedList<Action> l = new LinkedList<>();
            for (int i = 0; i <= pointer; i++) {
                l.add(actionRepository.get(i));
            }
            return l;
        }
        else {
            LinkedList<Action> l = new LinkedList<>();
            for (int i = pointer; i >= 0; i--) {
                if (actionRepository.get(i).getUserId() == u.getId()) {
                    l.add(actionRepository.get(i));
                }
                else {
                    break;
                }
            }
            return l;
        }
    }
    public List<Action> getActionRedoList(User u){
        if (u.getRoles().contains("ProjectManager")){
            LinkedList<Action> l = new LinkedList<>();
            for (int i = pointer+1; i < actionRepository.size(); i++) {
                l.add(actionRepository.get(i));
            }
            return l;
        }
        else {
            LinkedList<Action> l = new LinkedList<>();
            for (int i = pointer+1; i < actionRepository.size(); i++) {
                if (actionRepository.get(i).getUserId() == u.getId()) {
                    l.add(actionRepository.get(i));
                }
                else {
                    break;
                }
            }
            return l;
        }
    }

}
