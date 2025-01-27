package com.taskmanager.controller;


import com.taskmanager.model.User;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GRASP controller for the userservice
 */
public class UserService {
    private Map<Long,User> users = new HashMap<>();

    /**
     * Logs in a user with the given credentials
     * @param userName The username of the user trying to log in
     * @param password The password of the user trying to log in
     * @return The id of the user if the login is successful, -1 if the login failed
     */
    public Long logInUser(String userName, String password) {
        for (User u : users.values()){
            if (u.getUsername().equals(userName)){
                if (u.getPassword().equals(password)){
                    return u.getId();
                }
                else {
                    return -1L;
                }
            }
        }
        return -1L;
    };


    /**
     * Get all the roles of a user
     * @param id The id of the user
     * @return The list of roles of the user
     */
    public List<String> getRolesFromUserId(long id){
        return getUserById(id).getRoles();
    }


    /**
     * Adds a new user
     * @param username The username of the new user to be added
     * @param password The password of the new user to be added
     * @return The new user's id
     */
    public long addUser(String username,String password) {
        return addUser(new User(username,password));
    }


    /**
     * Gets a list of all existing users
     * @return A list of all the users
     */
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }


    /**
     * Adds a user
     * @param u The user to add
     * @return The new user's id
     * @throws IllegalArgumentException When the user is null
     */
    public long addUser(User u) {
        if (u == null) throw new IllegalArgumentException("User cant be null");
        users.put(u.getId(),u);
        return u.getId();
    }


    /**
     * Returns the user via the given user id
     * @param id The user's id
     * @return The user matching the id
     * @throws IllegalArgumentException When the user is not found
     */
    public User getUserById(long id) {
        if(users.get(id) ==null){throw new IllegalArgumentException("User not found");}
        return users.get(id);
    }

    /**
     * Replaces the user repository with the given one
     * @param userRepository The new user repository
     */
    public void replaceRepo(Map<Long, User> userRepository) {
        this.users = userRepository;
    }

    /**
     * Makes a deep clone of the given map
     * @param map The map to clone
     * @return A deep clone of the given map
     */
    private  Map<Long, User> deepCloneMap(Map<Long, User> map){
        Map<Long, User> mapCopy = map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> {
                    try {
                        return e.getValue().clone();
                    } catch (CloneNotSupportedException ex) {
                        throw new RuntimeException(ex);
                    }
                }));
        return mapCopy;
    }

    /**
     * Returns a map of all users
     * @return A deep clone map of all users
     */
    public Map<Long, User> getUsersAsMap() {
        return deepCloneMap(users);
    }
}
