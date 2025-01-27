package com.taskmanager.controller;

import com.taskmanager.model.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * GRASP controller for the roles
 */
public class RoleController {
    /**
     * The user service
     */
    private UserService userService;
    /**
     * The action repository
     */
    private ActionRepository actionRepository;
    /**
     * The project repository
     */
    private ProjectRepository projectRepository;
    /**
     * The task repository
     */
    private TaskRepository taskRepository;

    /**
     * Constructor for the role controller
     * @param userService The user service
     * @param actionRepository The action repository
     * @param projectRepository The project repository
     * @param taskRepository The task repository
     */
    public RoleController(UserService userService,ActionRepository actionRepository, ProjectRepository projectRepository,TaskRepository taskRepository){
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.actionRepository = actionRepository;
    }

    /**
     * Advance the time of clock and notify the observers
     * @param time The timestamp to advance to
     */
    public void advanceTime(Timestamp time) {
        Clock.setSystemTime(time);
    }


    /**
     * Get the system time
     * @return The clock's current time
     */
    public Timestamp getTime(){
        return Clock.getSystemTime();
    }


    /**
     * Get the actions of the user
     * @param userid The id of the user
     * @return The actions of the user
     */
    public List<Action> getActions(long userid){
        return actionRepository.getActionList(userService.getUserById(userid));
    }

    /**
     * Get the undo list of the user
     * @param userid The id of the user
     * @return The undo list of the user
     */
    public List<Action> getActionUndoList(long userid){
        return actionRepository.getActionUndoList(userService.getUserById(userid));
    }

    /**
     * Get the redo list of the user
     * @param userid The id of the user
     * @return The redo list of the user
     */
    public List<Action> getActionRedoList(long userid){
        return actionRepository.getActionRedoList(userService.getUserById(userid));
    }

    /**
     * Undo the last action
     * @param userid The id of the user
     */
    public void undoAction(long userid){
         actionRepository.undoAction(userService.getUserById(userid),projectRepository,taskRepository,userService);
    }

    /**
     * Redo the last action
     * @param userid The id of the user
     */
    public void redoAction(long userid){
        actionRepository.redoAction(userService.getUserById(userid),projectRepository,taskRepository,userService);
    }
}
