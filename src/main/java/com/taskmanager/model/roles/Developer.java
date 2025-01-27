package com.taskmanager.model.roles;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;

import java.util.*;


public class Developer extends Role{

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ActionRepository actionRepository;

    public Developer(TaskRepository taskRepository, UserService userService, ActionRepository actionRepository,ProjectRepository projectRepository){
        this.taskRepository = taskRepository ;
        this.userService = userService;
        this.actionRepository = actionRepository;
    }


    /**
     * Ends the given task, and indicates that it either finished or failed
     * @param taskId The id of the task to be ended
     * @param failed FINISHED, or FAILED
     */
    public void endTask(long taskId, boolean failed,long userid) {
        taskRepository.endTask(taskId, failed);
        String actionDescription = "Ended Task: " + taskRepository.getTaskById(taskId).getDescription();

        actionRepository.addAction(
                new Action(actionDescription,userid,
                        () -> taskRepository.undoEndTask(taskId),
                        () -> taskRepository.endTask(taskId, failed)));

    }

    public long addUserToTask(long taskId, long userid) {
        return addUserToTask(taskId, userid,null,"");
    }
    /**
     * Adds a user to a task, if he is not yet working on one
     *
     * @param taskId   The id of the task on which the user will start working
     * @param userid   The user that will start working on the task
     * @param prevTask
     * @return True if the user was already working on a task that is pending, False otherwise
     */
    public long addUserToTask(long taskId, long userid, Task prevTask,String prevKey) {
        User user = userService.getUserById(userid);
        Map<String, User> taskRoles = taskRepository.getTaskById(taskId).getNecessaryRoles();

        //check if the user has a role that is required for the task
        try {
            checkRoles(user,taskRoles);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(user.getWorkingTask() != null){
            if(user.getWorkingTask().getStatus().equals("PENDING"))
                return user.getWorkingTask().getId();
        }

        //assign easy roles (void and available for a role the user has)
        for (String role: user.getRoles()){
            for (String key: taskRoles.keySet()){
                if( key.matches(role + "\\d*") && taskRoles.get(key) == null){

                    String actiondesc = user.getUsername() + " assigned to " + taskRepository.getTaskById(taskId).getDescription();
                    actionRepository.addAction(
                            new Action(actiondesc,userid,
                                    () -> {
                                        this.forceRemoveUserFromTask(userid);
                                        if (prevTask != null){
                                            taskRepository.assignUserToTask(prevTask.getId(),user,prevKey);
                                        }

                                    } ,
                                    ()->  taskRepository.assignUserToTask(taskId,user,key)));

                    taskRepository.assignUserToTask(taskId,user,key);
                    return 0;
                }
            }
        }

        //reshuffle roles if no easy assignment is possible
        assignRoles(taskRepository.getTaskById(taskId),user, prevTask, prevKey);

        return 0;
    }

    private void forceRemoveUserFromTask(long userId) {
        User user = userService.getUserById(userId);
        Task task = user.getWorkingTask();
        user.removeWorkingTask();
        if (task != null){
            task.forceRemoveUserFromTask(user);
        }
        System.out.println("task was null when removing best to check line 106 developer.java");
    }

    /**
     * Try and find d suitable assigment of roles for the users assigned to this task.
     * if no suitable assignment is found, don't add the user and throw an exception
     * @param task the task which the user wants to start working on
     * @param user The user that has to be assigned
     * @throws RuntimeException if the user can't be assigned to the task
     */
    public void assignRoles(Task task, User user, Task prevTask,String prevKey){
        //The map which will contain the users and their roles
        Map<User, String> userRoles = new HashMap<>();
        //All the users assigned to a task
        List<User> registeredUsers = new ArrayList<>();
        //the roles the task needs
        List<String> taskRoles = new ArrayList<>(task.getNecessaryRoles().keySet());
        for(User u : task.getNecessaryRoles().values()){
            if(u != null){registeredUsers.add(u);}
        }
        registeredUsers.add(user);

        //recursive try and find a suitable mapping of users and roles
        assignRolesRecursive(taskRoles, registeredUsers, userRoles);
        if (userRoles.isEmpty()) {
           throw new RuntimeException("Can't assign this user to this task");
        }
        //un assign the working users from their current role
        for( User u : task.getNecessaryRoles().values()){
            if(u != null){
                task.removeUserFromTask(u);
                u.removeWorkingTask();
            }
        }
        //assign the new roles
        for (User u: userRoles.keySet()) {
            taskRepository.assignUserToTask(task.getId(),u,userRoles.get(u));
        }
        //Add start Task to the actions
        Long userid = user.getId();
        Long taskId = task.getId();
        String key = userRoles.get(user);

        String actiondesc = user.getUsername() + " assigned to " + taskRepository.getTaskById(taskId).getDescription();
        actionRepository.addAction(
                new Action(actiondesc,userid,
                        () -> {
                            this.forceRemoveUserFromTask(userid);
                            if (prevTask != null){
                                taskRepository.assignUserToTask(prevTask.getId(),user,prevKey);
                            }

                        } ,
                        ()->  taskRepository.assignUserToTask(taskId,user,key)));

    }

    /**
     * Recursively try to find a suitable map of users and roles for a task.
     * @param taskRoles all the roles the task needs
     * @param users the users assigned to the task
     * @param userRoles a map Map<User, String> for users and their roles
     * @return true if a suitable map is found, false otherwise
     */
    private boolean assignRolesRecursive(List<String> taskRoles, List<User> users, Map<User, String> userRoles){
            if (users.isEmpty()) {
                return true;
            }
            User user = (User) users.toArray()[0];
            List<String> roles = user.getRoles();
            for (String role : roles) {
                for (String key: taskRoles){
                    if(key.matches(role + "\\d*")){
                        userRoles.put(user, key);
                        List<User> remainingUsers = new ArrayList<>(users);
                        List<String> remainingRoles = new ArrayList<>(taskRoles);
                        remainingUsers.remove(user);
                        remainingRoles.remove(key);
                        if (assignRolesRecursive(remainingRoles, remainingUsers, userRoles)) {
                            return true;
                        }
                        userRoles.remove(user);
                    }
                }
            }
            return false;
        }

    /**
     * Check if the provided user has a role that is required for the task
     * @param user the user
     * @param taskRoles a map of the roles of a task and the user assigned to that task
     * @throws Exception if the user has no role that is required for the task
     */
    public void checkRoles(User user, Map<String, User> taskRoles) throws Exception {
        for(String role: user.getRoles()){
            for(String key : taskRoles.keySet()){
                //Developer0 ex in the key
                if(key.matches(role + "\\d*"))
                    return;
            }
        }
        throw new Exception("User does not have the required role to work on this task");
    }

    /**
     * Creates and returns a list of
     * @param userId The user's id of which a list of tasks (s)he's working on should be returned
     * @return A list of all the tasks the user is currently working on
     */
    public List<Task> getWorkingTasks(long userId) {
        List<Task> tasks = taskRepository.getTasks();
        List<Task> workingTasks = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getStatus().equals("EXECUTING")){
                for(User user: (task.getNecessaryRoles().values())){
                    if(user != null && user.getId() == userId) {
                        workingTasks.add(task);
                    }
                }
            }
        }
        return workingTasks;
    }


    /**
     * Removes a user from a task completely
     * @param userId The user of which the working task should be removed
     */
    public void removeUserFromTask(long userId) {
        User user = userService.getUserById(userId);
        Task task = user.getWorkingTask();
        if (task == null){
            throw new IllegalArgumentException("User is not working on a task");
        }
        user.removeWorkingTask();
        task.removeUserFromTask(user);
    }

    /**
     * Find the task in the task repository with the given unique task id
     * @param taskId the id of the task to be returned
     * @return the task with the given id
     */
    public Task getTaskById(Long taskId) {
        return taskRepository.getTaskById(taskId);
    }

    /**
     * Returns a list of all existing tasks inside the task repository
     * @return a complete list of every existing tasks inside the repository
     */
    public List<Task> getTasks() {
        return taskRepository.getTasks();
    }


    /**
     * Returns a user via the given identifier
     * @param userById the id of the user to be returned
     * @return the user with the matching id
     */
    public User getUserById(long userById) {
        return userService.getUserById(userById);
    }
}
