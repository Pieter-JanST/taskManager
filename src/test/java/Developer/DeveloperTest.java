package Developer;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;
import com.taskmanager.model.roles.Developer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;

public class DeveloperTest {
    private TaskRepository taskRepository = new TaskRepository();
    ProjectRepository projectRepository = new ProjectRepository();
    private UserService userService = new UserService();
    @Test
    public void endTaskTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        User u = new User("u","u",List.of("JavaProgrammer"));
        userService.addUser(u);
        taskRepository.addTask(t1);
        Developer d = new Developer(taskRepository,userService,new ActionRepository(),projectRepository);
        d.addUserToTask(t1.getId(),u.getId());//, "JavaProgrammer0"
        Assertions.assertEquals(u.getWorkingTask().getId(),t1.getId());
        Assertions.assertEquals("EXECUTING",t1.getStatus());
        d.endTask(t1.getId(),false,u.getId());
        Assertions.assertEquals("FINISHED",t1.getStatus());
        Assertions.assertNull(u.getWorkingTask());
        Assertions.assertTrue(t1.getNecessaryRoles().containsValue(u));
    }
    @Test
    public void addUserToTaskTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        User u = new User("u","u",List.of("JavaProgrammer"));
        userService.addUser(u);
        taskRepository.addTask(t1);
        Developer d = new Developer(taskRepository,userService,new ActionRepository(),projectRepository);
        d.addUserToTask(t1.getId(),u.getId());//, "JavaProgrammer0"
        Assertions.assertEquals(u.getWorkingTask().getId(),t1.getId());
        Assertions.assertEquals("EXECUTING",t1.getStatus());
    }
    @Test
    public void getWorkingTasksTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project, getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        User u = new User("u","u",List.of("JavaProgrammer"));
        userService.addUser(u);
        taskRepository.addTask(t1);
        Developer d = new Developer(taskRepository,userService,new ActionRepository(),projectRepository);
        d.addUserToTask(t1.getId(),u.getId());//, "JavaProgrammer0"
        Assertions.assertTrue(d.getWorkingTasks(u.getId()).contains(t1));
    }

    /**
     * Test whether the system is able to change the assigment of users in tasks so the roles match up.
     */
    @Test
    public void addMultipleUsersToTaskTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project,getSystemTime(), List.of(("JavaProgrammer,PythonProgrammer").split(",")),new ArrayList<>());
        User u = new User("u","u",List.of(("JavaProgrammer,PythonProgrammer").split(",")));
        User u2 = new User("d","d",List.of("JavaProgrammer"));
        userService.addUser(u);
        userService.addUser(u2);
        taskRepository.addTask(t1);
        Developer d = new Developer(taskRepository,userService,new ActionRepository(),projectRepository);
        d.addUserToTask(t1.getId(),u.getId());//, "JavaProgrammer0"
        //assigns JavaProgrammer first, when assigning u2 it will have to switch cause u2 has JavaProgrammer but not PythonProgrammer
        d.addUserToTask(t1.getId(),u2.getId());
        Assertions.assertEquals(u.getWorkingTask().getId(),t1.getId());
        Assertions.assertEquals("EXECUTING",t1.getStatus());
        Assertions.assertFalse(t1.getNecessaryRoles().containsValue(null));
    }

}
