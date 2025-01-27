package com.taskmanager.model.roles;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.ActionRepository;
import com.taskmanager.model.ProjectRepository;
import com.taskmanager.model.TaskRepository;

public class Programmer extends Developer{

    public Programmer(TaskRepository taskRepository, UserService userService, ActionRepository actionRepository, ProjectRepository projectRepository) {
        super(taskRepository, userService,actionRepository,projectRepository);
    }
}
