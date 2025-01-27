package com.taskmanager;

import com.taskmanager.controller.PMController;
import com.taskmanager.controller.UserService;
import com.taskmanager.model.Project;
import com.taskmanager.model.User;


import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for seeding the start-up data
 */
public class Seeder {

    PMController pmController;
    UserService userService;

    /**
     * Constructor for the seeder
     * @param pmController the pmController
     * @param userService the userService
     */
    public Seeder( PMController pmController, UserService userService){
        this.pmController =  pmController;
        this.userService =  userService;
    }


    /**
     * Add the users defined in the file
     */
    public void seed() {
        ClassLoader loader = this.getClass().getClassLoader();

        //trying to find the right directory (with tomcat being a bitch)
        String dir = String.valueOf(loader.getResource("com/taskmanager/Seeder.class"));
        dir = dir.split("com/taskmanager/Seeder.class")[0];
        dir = dir.split("file:/")[1];
        dir= dir.replace("%20"," ");
        dir+= "seeds";

        readUsers(dir);
        readProjects(dir);
        readTasks(dir);

        System.out.println("succesfully seeded data");
        System.out.println("system succesfully started up");
        System.out.println("server is now running on localhost:8080");

    }

    /**
     * Add the projects defined in the file
     * @param dir the directory of the files
     * @return true if all goes well
     */
    public boolean readProjects(String dir)
    {
        try {
            File myObj = new File(dir+"/Project.txt");

            Scanner myReader = new Scanner(myObj);
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] params = data.split("\\|");
                Timestamp time = new Timestamp(System.currentTimeMillis());
                Project p = new Project(params[0],params[1],Timestamp.valueOf(params[3]), time, time);
                p.setCreationTime(Timestamp.valueOf(params[2]));
                pmController.addProject(params[0], params[1], params[3],1);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error while reading projects.");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Add the users defined in the file
     * @param dir the directory of the files
     * @return true if all goes well
     */
    public boolean readUsers(String dir)
    {
        try {
            File myObj = new File(dir+"/User.txt");

            Scanner myReader = new Scanner(myObj);
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] params = data.split("\\|");

                User u = new User(params[0],params[1]);
                if (params.length > 2){
                    for (String r :params[2].split(",")) {
                        u.addRole(r);
                    }

                }
                userService.addUser(u);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error while reading projects.");
            e.printStackTrace();
        }
        return true;
    }
    /**
     * Add the tasks defined in the file
     * @param dir the directory of the files
     * @return true if all goes well
     */
    public boolean readTasks(String dir){

        try {
            File myObj = new File(dir+"/Task.txt");

            Scanner myReader = new Scanner(myObj);
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] params = data.split("\\|");
                long projectId = Long.parseLong(params[0]);
                if (params.length > 5){
                    String[] dependsOnsString = params[5].split(",");
                    List<Long> dependsOns = new LinkedList<>();
                    for (String s :dependsOnsString) {
                        if (!s.equals("-1")){
                            dependsOns.add(Long.valueOf(s));
                        }
                    }

                    pmController.addTask(Long.parseLong(params[0]),params[1],Integer.parseInt(params[2]),Float.parseFloat(params[3]), List.of(params[4].split(",")), dependsOns,1);
                }
                else {
                    pmController.addTask(Long.parseLong(params[0]),params[1],Integer.parseInt(params[2]),Float.parseFloat(params[3]), List.of(params[4].split(",")), new LinkedList<>(),1);

                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error while reading tasks.");
            e.printStackTrace();
        }
        return true;
    }

}